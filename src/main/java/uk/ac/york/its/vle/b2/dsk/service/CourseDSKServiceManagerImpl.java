/**
 * Copyright (C) 2016 University of York, UK.
 * <p>
 * This project was initiated through a donation of source code by the
 * University of York, UK. It contains free software; you can redistribute
 * it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the
 * License, or any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * <p>
 * For more information please contact:
 * <p>
 * Web Services Group
 * IT Service
 * University of York
 * YO10 5DD
 * United Kingdom
 */
package uk.ac.york.its.vle.b2.dsk.service;

/**
 * @author Kelvin Hai {@link <a href="mailto:kelvin.hai@york.ac.uk">kelvin.hai@york.ac.uk</a>}
 * @version $Revision$ $Date$
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.ac.york.its.vle.b2.dsk.data.*;
import blackboard.admin.data.IAdminObject.RowStatus;
import blackboard.admin.data.category.CourseCategoryMembership;
import blackboard.admin.data.category.OrganizationCategoryMembership;
import blackboard.admin.data.course.CourseSite;
import blackboard.admin.persist.category.CourseCategoryLoader;
//import blackboard.admin.persist.category.CourseCategoryMembershipLoader;
//import blackboard.admin.persist.category.OrganizationCategoryMembershipLoader;
import blackboard.admin.persist.course.CourseSiteLoader;

import blackboard.admin.persist.course.CourseSitePersister;
import blackboard.data.ValidationException;
import blackboard.data.course.Course;
import blackboard.db.ConstraintViolationException;
import blackboard.persist.Id;
import blackboard.persist.KeyNotFoundException;
import blackboard.persist.PersistenceException;
import blackboard.persist.SearchOperator;
import blackboard.persist.course.CourseDbLoader;
import blackboard.persist.course.CourseSearch;
import blackboard.util.StringUtil;
import uk.ac.york.its.vle.b2.dsk.model.PersistenceOutcome;

@Service
public class CourseDSKServiceManagerImpl implements CourseDSKServiceManager {
    private static final Logger logger = LoggerFactory.getLogger(CourseDSKServiceManagerImpl.class);

    @Autowired
    private ServiceManager serviceManager;

    @Autowired
    private CourseDbLoader courseDbLoader;

    @Autowired
    private CourseSiteLoader courseSiteLoader;

    @Autowired
    private CourseSitePersister courseSitePersister;

    // @Autowired
    // private CourseCategoryLoader courseCategoryLoader;

//    @Autowired
//    private CourseCategoryMembershipLoader courseCategoryMembershipLoader;

    // @Autowired
    // private OrganizationCategoryMembershipLoader organisationCategoryMembershipLoader;

    @Autowired
    private ReferenceServiceManager referenceServiceManager;

    public List<B2Course> getCourses(Form form, String sortByPropertyName) {
        return getCourses(form, sortByPropertyName, Course.ServiceLevel.FULL);
    }

    public List<B2Course> getOrganisations(Form form, String sortByPropertyName) {
        return getCourses(form, sortByPropertyName, Course.ServiceLevel.COMMUNITY);
    }

    // public List<B2Course> getCoursesByCourseCategory(Form form, String sortByPropertyName) {
    //     if (form != null) {
    //         //CourseSite needs to be used in order to load Disabled courses
    //         List<Course> courses = getCoursesByCourseCategoryBatchUid(form.getCategory());
    //         return getB2Courses(courses, form, sortByPropertyName);
    //     }
    //     return null;
    // }

    // public List<B2Course> getCoursesByOrganisationCategory(Form form, String sortByPropertyName) {
    //     if (form != null) {
    //         //CourseSite needs to be used in order to load Disabled courses
    //         List<Course> courses = getCoursesByOrganisationCategoryBatchUid(form.getCategory());
    //         return getB2Courses(courses, form, sortByPropertyName);
    //     }
    //     return null;
    // }

    private List<B2Course> getB2Courses(List<Course> courses, Form form, String sortByPropertyName) {
        if (null != courses) {
            serviceManager.sortCourses(courses, sortByPropertyName);
            Map<String, String> dskMap = referenceServiceManager.getDataSourceKeysMap();
            List<B2Course> b2Courses = new ArrayList<B2Course>();
            SearchOperator dateOperator = getCourseDateOperator(form);

            for (Course course : courses) {
                //course.getBatchUid always return null
                boolean isCourseValid = false;
                if (null != course) {
                    if (SearchOperator.LessThan.equals(dateOperator)) {
                        if (course.getCreatedDate().before(form.getCreateDateCalendar())) {
                            isCourseValid = true;
                        }
                    } else if (SearchOperator.GreaterThan.equals(dateOperator)) {
                        if (course.getCreatedDate().after(form.getCreateDateCalendar())) {
                            isCourseValid = true;
                        }
                    }

                    if (isCourseValid) {
                        b2Courses.add(getB2Course(course, dskMap));
                    }
                }
            }

            if (null != b2Courses && b2Courses.size() > 0) {
                return b2Courses;
            }
        }
        return null;
    }

    private List<PersistenceOutcome> persistCourseSite(String[] selectedCourseSiteBatchUids, Form form) {
        List<PersistenceOutcome> outcomes = null;
        if (null != form && null != selectedCourseSiteBatchUids && selectedCourseSiteBatchUids.length > 0) {
            logger.info(Parameter.CHANGE_COMMENTS_TEXTAREA_KEY.getName()+": "+form.getChangeComments());
            outcomes = new ArrayList<PersistenceOutcome>();
            PersistenceOutcome outcome = null;
            boolean isHeaderLogged = false;
            for (String batchUid : selectedCourseSiteBatchUids) {
                outcome = new PersistenceOutcome();
                if (StringUtil.notEmpty(batchUid)) {
                    CourseSite loadedCourseSite = null;
                    try {
                        loadedCourseSite = courseSiteLoader.load(batchUid);
                    } catch (KeyNotFoundException e) {
                        logger.error("Method: persistCourseSite: loadedCourseSite: KeyNotFoundException: " + e.getMessage());
                    } catch (PersistenceException e) {
                        logger.error("Method: persistCourseSite: loadedCourseSite: PersistenceException: " + e.getMessage());
                    }

                    if (null != loadedCourseSite) {

                        if (!isHeaderLogged) {
                            logger.info(outcome.getHeader());
                            isHeaderLogged = true;
                        }
                        log(loadedCourseSite);
                        outcomes.add(getPersistenceOutcome(loadedCourseSite, true));

                        outcome.setCourseId(loadedCourseSite.getCourseId());
                        outcome.setCourseTitle(loadedCourseSite.getTitle());
                        outcome.setCourseBatchUid(loadedCourseSite.getBatchUid());
                        outcome.setDataSourceKey(loadedCourseSite.getDataSourceBatchUid());
                        outcome.setRowStatus(loadedCourseSite.getRowStatus().toFieldName());
                        outcome.setIsAvailable(loadedCourseSite.getIsAvailable());

                        String selectedAvailability = null, selectedRowStatus = null, selectedDataSourceKey = null;


                        if (form.getIsAvailabilityUpdateRequired()) {
                            if (StringUtil.notEmpty(selectedAvailability = form.getSelectedAvailability())
                                    && selectedAvailability.trim().equals(B2Enum.Available.getName())) {
                                outcome.setIsAvailable(true);
                                loadedCourseSite.setIsAvailable(true);
                            } else {
                                outcome.setIsAvailable(false);
                                loadedCourseSite.setIsAvailable(false);
                            }
                        }

                        if (form.getIsStatusUpdateRequired()) {
                            if (StringUtil.notEmpty(selectedRowStatus = form.getSelectedRowStatus())
                                    && selectedRowStatus.trim().equals(B2Enum.Enabled.getName())) {
                                outcome.setRowStatus(RowStatus.ENABLED.toFieldName());
                                loadedCourseSite.setRowStatus(RowStatus.ENABLED);
                            } else {
                                outcome.setRowStatus(RowStatus.DISABLED.toFieldName());
                                loadedCourseSite.setRowStatus(RowStatus.DISABLED);
                            }
                        }

                        if (form.getIsDataSourceKeyUpdateRequired()) {
                            if (StringUtil.notEmpty(selectedDataSourceKey = form.getSelectedDataSourceKey())) {
                                loadedCourseSite.setDataSourceBatchUid(selectedDataSourceKey.trim());
                                outcome.setDataSourceKey(selectedDataSourceKey.trim());
                            }
                        }


                        try {
                            courseSitePersister.save(loadedCourseSite);
                            outcome.setIsPersistedSuccess(true);
                            log(outcome);
                        } catch (PersistenceException e) {
                            logger.error("Method: persistCourseSite: courseSitePersister: PersistenceException: " + e.getMessage());
                        } catch (ConstraintViolationException e) {
                            logger.error("Method: persistCourseSite: courseSitePersister: ConstraintViolationException: " + e.getMessage());
                        } catch (ValidationException e) {
                            logger.error("Method: persistCourseSite: ValidationException: " + e.getMessage());
                        }
                    }
                }
                outcomes.add(outcome);
            }
        }
        return outcomes;
    }

    public List<PersistenceOutcome> persistCourse(String[] selectedCourseIds, Form form) {
        return persistCourseSite(selectedCourseBatchUids(selectedCourseIds), form);
    }

    // to workaround result courses with loadByCourseSearch does not have batchUid info.
    private String[] selectedCourseBatchUids(String[] selectedCourseIds) {
        String[] selectedCourseBatchUids = null;
        if (ArrayUtils.isNotEmpty(selectedCourseIds)) {
            selectedCourseBatchUids = new String[selectedCourseIds.length];
            int index = 0;
            for (String courseId : selectedCourseIds) {
                CourseSite courseSite = null;
                if (null != (courseSite = serviceManager.getCourseSiteByCourseSiteId(courseId))) {
                    selectedCourseBatchUids[index++] = courseSite.getBatchUid();
                }
            }
        }
        return selectedCourseBatchUids;
    }

    private List<B2Course> getCourses(Form form, String sortByPropertyName, Course.ServiceLevel courseServiceLevel) {
        List<Course> courses = getCoursesByCourseSearch(form, courseServiceLevel);
        //course.getBatchUid always return null if courseDbLoader.loadByCourseSearch is used
        if (null != courses && courses.size() > 0) {
            serviceManager.sortCourses(courses, sortByPropertyName);
            Map<String, String> dskMap = referenceServiceManager.getDataSourceKeysMap();
            List<B2Course> b2Courses = new ArrayList<B2Course>();

            for (Course course : courses) {
                if (null != course) {
                    b2Courses.add(getB2Course(course, dskMap));
                }
            }

            if (null != b2Courses && b2Courses.size() > 0) {
                return b2Courses;
            }
        }
        return null;
    }

    private B2Course getB2Course(Course course, Map<String, String> dskMap) {
        if (null != course) {
            B2Course b2Course = new B2Course(course);

            //course.getBatchUid always return null
            String courseBatchUid = null;
            //Obtain batch_uid from BbAttributes if possible to avoid performance impact.
            // Look like courseDbLoader.loadByBatchUid(membership.getCourseSiteBatchUid()
            // or courseDbLoader.loadByBatchUid(membership.getOrganizationBatchUid()
            // will return BbAttributes containing BatchUid, but not courseDbLoader.loadByCourseSearch(courseSearch)
            try {
                Object batcUidAttribute = course.getBbAttributes().getBbAttribute("BatchUid").getValue();
                if (null != batcUidAttribute) {
                    courseBatchUid = batcUidAttribute.toString();
                }
            } catch (NullPointerException e) {
            }
            //If batch_uid does not exist in BbAttributes, then load the courseSite to obtain it.
            if (StringUtil.notEmpty(courseBatchUid)) {
                b2Course.setCourseBatchUid(courseBatchUid);
            } else {
                CourseSite courseSite = serviceManager.getCourseSiteByCourseSiteId(course.getCourseId());
                if (null != courseSite) {
                    b2Course.setCourseBatchUid(courseSite.getBatchUid());
                }
            }

            try {
                //Course does not have the getRowStatus method
                if (!("0").equals(course.getBbAttributes().getBbAttribute("RowStatus").getValue().toString())) {
                    b2Course.setIsCourseDisabled(true);
                }
            } catch (NullPointerException e) {
            }

            if (null != dskMap) {
                b2Course.setDataSourceKey(dskMap.get(course.getDataSourceId().getExternalString()));
            }
            return b2Course;
        }

        return null;
    }

    private List<Course> getCoursesByCourseSearch(Form form, Course.ServiceLevel courseServiceLevel) {
        CourseSearch courseSearch = null;

        if (null != form && StringUtil.notEmpty(form.getSearchText())
                && null != (courseSearch = getCourseSearch(form, courseServiceLevel))) {
            try {
                return courseDbLoader.loadByCourseSearch(courseSearch);
            } catch (PersistenceException e) {
                logger.error("Method: getCoursesByCourseSearch: " + e.getMessage());
            }
        }
        return null;
    }

    private CourseSearch getCourseSearch(Form form, Course.ServiceLevel courseServiceLevel) {

        CourseSearch.SearchKey searchKey = getCourseSearchKey(form);
        SearchOperator courseSearchOperator = getCourseSearchOperator(form);
        String searchText = form.getSearchText();
        String selectedCategory = form.getCategory();

        if (CourseSearch.SearchKey.Instructor.equals(searchKey)) {
            return CourseSearch.getInstructorSearch(courseSearchOperator, form.getSearchText(), courseServiceLevel);
        } else if (StringUtil.notEmpty(selectedCategory) && !selectedCategory.trim().equals("-1")) {

            Id categoryId = null;
                return CourseSearch.getInfoSearch(searchKey, courseSearchOperator, searchText, courseServiceLevel);
        } else if (null != form.getCreateDateCalendar()) {
            return CourseSearch.getViewCoursesSearch(searchKey, courseSearchOperator, searchText, getCourseDateOperator(form), form.getCreateDateCalendar().getTime(), courseServiceLevel);
        } else {
            return CourseSearch.getInfoSearch(searchKey, courseSearchOperator, searchText, courseServiceLevel);
        }
    }

    private CourseSearch.SearchKey getCourseSearchKey(Form form) {
        String searchKey = null;
        if (StringUtil.notEmpty(searchKey = form.getSearchKey())) {
            if (searchKey.equals(CourseSearch.SearchKey.CourseId.name())) {
                return CourseSearch.SearchKey.CourseId;
            } else if (searchKey.equals(CourseSearch.SearchKey.CourseName.name())) {
                return CourseSearch.SearchKey.CourseName;
            } else if (searchKey.equals(CourseSearch.SearchKey.CourseDescription.name())) {
                return CourseSearch.SearchKey.CourseDescription;
            } else if (searchKey.equals(CourseSearch.SearchKey.Instructor.name())) {
                return CourseSearch.SearchKey.Instructor;
            // } else if (searchKey.equals(CourseSearch.SearchKey.CategoryId.name())) {
            //     return CourseSearch.SearchKey.CategoryId;
            } else if (searchKey.equals(CourseSearch.SearchKey.DataSourceKey.name())) {
                return CourseSearch.SearchKey.DataSourceKey;
            }
        }
        return null;
    }

    private SearchOperator getCourseSearchOperator(Form form) {
        String searchOperator = null;
        if (StringUtil.notEmpty(searchOperator = form.getSearchOperator())) {
            if (searchOperator.equals(SearchOperator.Contains.name())) {
                return SearchOperator.Contains;
            } else if (searchOperator.equals(SearchOperator.StartsWith.name())) {
                return SearchOperator.StartsWith;
            } else if (searchOperator.equals(SearchOperator.Equals.name())) {
                return SearchOperator.Equals;
            }
        }
        return SearchOperator.Equals;
    }

    private SearchOperator getCourseDateOperator(Form form) {
        String searchOperator = null;
        if (StringUtil.notEmpty(searchOperator = form.getDateSearchOperator())) {
            if (searchOperator.equals(SearchOperator.LessThan.name())) {
                return SearchOperator.LessThan;
            } else if (searchOperator.equals(SearchOperator.GreaterThan.name())) {
                return SearchOperator.GreaterThan;
            }
        }
        return SearchOperator.LessThan;
    }

/*     private List<CourseCategoryMembership> getCourseCategoryMemberships(String categoryBatchUid) {
        if (StringUtil.notEmpty(categoryBatchUid)) {
            CourseCategoryMembership courseCategoryMembership = new CourseCategoryMembership();
            courseCategoryMembership.setCategoryBatchUid(categoryBatchUid.trim());
            try {
                return courseCategoryMembershipLoader.load(courseCategoryMembership);
            } catch (PersistenceException e) {
                logger.error("Method: getCourseCategoryMemberships: PersistenceException " + e.getMessage());
            }
        }
        return null;
    }
 */
/*     private List<OrganizationCategoryMembership> getOrganisationCategoryMemberships(String categoryBatchUid) {
        if (StringUtil.notEmpty(categoryBatchUid)) {
            OrganizationCategoryMembership organizationCategoryMembership = new OrganizationCategoryMembership();
            organizationCategoryMembership.setCategoryBatchUid(categoryBatchUid.trim());
            try {
                return organisationCategoryMembershipLoader.load(organizationCategoryMembership);
            } catch (PersistenceException e) {
                logger.error("Method: getOrganisationCategoryMemberships: PersistenceException " + e.getMessage());
            }
        }
        return null;
    } */

    // private List<Course> getCoursesByCourseCategoryBatchUid(String categoryBatchUid) {
    //     //course.getBatchUid always return null if courseDbLoader.loadByCourseSearch is used
    //     List<Course> courses = null;
    //     List<CourseCategoryMembership> courseCategoryMemberships = getCourseCategoryMemberships(categoryBatchUid);
    //     if (courseCategoryMemberships != null && courseCategoryMemberships.size() > 0) {
    //         courses = new ArrayList<Course>();
    //         for (CourseCategoryMembership membership : courseCategoryMemberships) {
    //             try {
    //                 //courseDbLoader.loadByCourseId does not load Disabled Course
    //                 //courseDbLoader.loadByBatchUid will load Disabled Course
    //                 courses.add(courseDbLoader.loadByBatchUid(membership.getCourseSiteBatchUid()));
    //             } catch (KeyNotFoundException e) {
    //                 logger.error("Method: getCourseSitesByCourseCategoryId: KeyNotFoundException " + e.getMessage());
    //             } catch (PersistenceException e) {
    //                 logger.error("Method: getCourseSitesByCourseCategoryId: PersistenceException " + e.getMessage());
    //             }
    //         }
    //     }

    //     return courses;
    // }

    // private List<Course> getCoursesByOrganisationCategoryBatchUid(String categoryBatchUid) {
    //     List<Course> courses = null;
    //     List<OrganizationCategoryMembership> organisationCategoryMemberships = getOrganisationCategoryMemberships(categoryBatchUid);
    //     if (organisationCategoryMemberships != null && organisationCategoryMemberships.size() > 0) {
    //         courses = new ArrayList<Course>();
    //         for (OrganizationCategoryMembership membership : organisationCategoryMemberships) {
    //             try {
    //                 //courseDbLoader.loadByCourseId does not load Disabled Course
    //                 //courseDbLoader.loadByBatchUid will load Disabled Course
    //                 courses.add(courseDbLoader.loadByBatchUid(membership.getOrganizationBatchUid()));
    //                 //courses.add((Course)organisationDbLoader.load(membership.getOrganizationBatchUid()));
    //             } catch (KeyNotFoundException e) {
    //                 logger.error("Method: getCoursesByOrganisationCategoryBatchUid: KeyNotFoundException " + e.getMessage());
    //             } catch (PersistenceException e) {
    //                 logger.error("Method: getCoursesByOrganisationCategoryBatchUid: PersistenceException " + e.getMessage());
    //             }
    //         }
    //     }

    //     return courses;
    // }


    private void log(CourseSite courseSite, PersistenceOutcome outcome) {
        log(courseSite);
        log(outcome);
    }


    private void log(CourseSite courseSite) {
        PersistenceOutcome origin = getPersistenceOutcome(courseSite, true);
        if (null != origin) {
            logger.info("ORIGIN: " + origin.toString());
        }
    }

    private void log(PersistenceOutcome outcome) {
        if (null != outcome) {
            logger.info("OUTCOME: " + outcome.toString());
        }
    }

    private PersistenceOutcome getPersistenceOutcome(CourseSite courseSite, boolean isBeforeUpdate) {
        if (null != courseSite) {
            PersistenceOutcome outcome = new PersistenceOutcome();
            outcome.setCourseId(courseSite.getCourseId());
            outcome.setCourseTitle(courseSite.getTitle());
            outcome.setCourseBatchUid(courseSite.getBatchUid());
            outcome.setDataSourceKey(courseSite.getDataSourceBatchUid());
            outcome.setRowStatus(courseSite.getRowStatus().toFieldName());
            outcome.setDataSourceKey(courseSite.getDataSourceBatchUid());
            outcome.setIsAvailable(courseSite.getIsAvailable());
            if(isBeforeUpdate){
                outcome.setIsBeforeUpdate(isBeforeUpdate);
            }

            return outcome;
        }
        return null;
    }

}
