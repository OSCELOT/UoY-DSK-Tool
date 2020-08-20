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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import blackboard.admin.data.datasource.DataSource;
import blackboard.admin.persist.datasource.DataSourceManager;
import blackboard.admin.persist.datasource.DataSourceManagerFactory;
import blackboard.persist.course.impl.CourseMembershipDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.ac.york.its.vle.b2.dsk.data.*;
import blackboard.admin.data.IAdminObject.RowStatus;
import blackboard.admin.data.category.CourseCategoryMembership;
import blackboard.admin.data.category.OrganizationCategoryMembership;
import blackboard.admin.data.course.CourseSite;
import blackboard.admin.data.course.Enrollment;
import blackboard.admin.data.user.Person;
import blackboard.admin.persist.category.CourseCategoryMembershipLoader;
import blackboard.admin.persist.category.OrganizationCategoryMembershipLoader;
import blackboard.admin.persist.course.EnrollmentLoader;
import blackboard.admin.persist.course.EnrollmentPersister;
import blackboard.admin.persist.user.PersonLoader;
import blackboard.data.ValidationException;
import blackboard.data.course.Course;
import blackboard.data.course.Course.ServiceLevel;
import blackboard.data.course.CourseMembership;
import blackboard.db.ConstraintViolationException;
import blackboard.persist.Id;
import blackboard.persist.KeyNotFoundException;
import blackboard.persist.PersistenceException;
import blackboard.persist.SearchOperator;
import blackboard.persist.course.CourseDbLoader;
import blackboard.persist.course.CourseMembershipDbLoader;
import blackboard.persist.course.CourseMembershipDbPersister;
import blackboard.persist.course.CourseSearch;
import blackboard.util.StringUtil;
import uk.ac.york.its.vle.b2.dsk.model.PersistenceOutcome;

@Service
public class EnrolmentDSKServiceManagerImpl implements EnrolmentDSKServiceManager {

    private static final Logger logger = LoggerFactory.getLogger(EnrolmentDSKServiceManagerImpl.class);

    @Resource
    private Map<CourseMembership.Role, String> courseRolesMap;

    @Resource
    private Map<CourseMembership.Role, String> organisationRolesMap;

    @Autowired
    private ServiceManager serviceManager;

    @Autowired
    private PersonLoader personLoader;

    @Autowired
    private CourseDbLoader courseDbLoader;

    @Autowired
    private CourseCategoryMembershipLoader courseCategoryMembershipLoader;

    @Autowired
    private OrganizationCategoryMembershipLoader organizationCategoryMembershipLoader;

    @Autowired
    private EnrollmentLoader enrollmentLoader;

    @Autowired
    private EnrollmentPersister enrollmentPersister;

    private DataSourceManager dataSourceManager = DataSourceManagerFactory.getInstance();


    public List<PersistenceOutcome> persistEnrolmentsByDAO(String[] selectedEnrollmentIds, Form form, String enrolmentType) {
        List<PersistenceOutcome> outcomes = null;
        if (null != form && form.getIsUpdateRequired() && null != selectedEnrollmentIds && selectedEnrollmentIds.length > 0) {
            logger.info(Parameter.CHANGE_COMMENTS_TEXTAREA_KEY.getName() + ": " + form.getChangeComments());
            outcomes = new ArrayList<PersistenceOutcome>();
            PersistenceOutcome outcome = null;
            boolean isHeaderLogged = false;
            CourseMembershipDAO courseMembershipDAO = new CourseMembershipDAO();
            DataSource dataSource = null;
            for (String idString : selectedEnrollmentIds) {
                if (StringUtil.notEmpty(idString)) {

                    Id id = null;
                    try {
                        id = Id.generateId(CourseMembership.DATA_TYPE, idString.trim());

                    } catch (PersistenceException e) {
                        logger.error("Method: persistEnrollments: PersistenceException: " + e.getMessage());
                    }
                    if (null != id) {

                        Enrollment loadedEnrolment = loadEnrolment(idString);
                        // record status before update
                        outcomes.add(getPersistenceOutcome(loadedEnrolment, enrolmentType, true));

                        // outcome after update
                        outcome = new PersistenceOutcome();
                        if (!isHeaderLogged) {
                            logger.info(outcome.getHeader());
                            isHeaderLogged = true;
                        }
                        log(loadedEnrolment, enrolmentType);

                        outcome.setUserBatchUid(loadedEnrolment.getPersonBatchUid());
                        outcome.setCourseBatchUid(loadedEnrolment.getCourseSiteBatchUid());
                        outcome.setDataSourceKey(loadedEnrolment.getDataSourceBatchUid());
                        outcome.setRowStatus(loadedEnrolment.getRowStatus().toFieldName());
                        outcome.setIsAvailable(loadedEnrolment.getIsAvailable());



                        CourseMembership loadedCourseMembership = null;
                        try {
                            loadedCourseMembership = courseMembershipDAO.loadById(id);
                        } catch (KeyNotFoundException e) {
                            logger.error("Method: persistEnrollmentsByDAO: KeyNotFoundException: " + e.getFullMessageTrace());
                        }

                        if (null != loadedCourseMembership){

                            String selectedAvailability = null, selectedRowStatus = null, selectedDataSourceKey = null, selectedCourseRole=null;


                            if (form.getIsAvailabilityUpdateRequired()) {
                                if (StringUtil.notEmpty(selectedAvailability = form.getSelectedAvailability())
                                        && selectedAvailability.trim().equals(B2Enum.Available.getName())) {
                                    outcome.setIsAvailable(true);
                                    loadedCourseMembership.setIsAvailable(true);
                                } else {
                                    outcome.setIsAvailable(false);
                                    loadedCourseMembership.setIsAvailable(false);
                                }
                            }

                            if (form.getIsStatusUpdateRequired()) {
                                if (StringUtil.notEmpty(selectedRowStatus = form.getSelectedRowStatus())
                                        && selectedRowStatus.trim().equals(B2Enum.Enabled.getName())) {
                                    outcome.setRowStatus(RowStatus.ENABLED.toFieldName());
                                    loadedCourseMembership.getBbAttributes().getBbAttribute("RowStatus").setValue(0);
                                } else {
                                    outcome.setRowStatus(RowStatus.DISABLED.toFieldName());
                                    loadedCourseMembership.getBbAttributes().getBbAttribute("RowStatus").setValue(2);
                                }
                            }

                            if (form.getIsDataSourceKeyUpdateRequired()) {
                                if (StringUtil.notEmpty(selectedDataSourceKey = form.getSelectedDataSourceKey())) {
                                    try {
                                        dataSource = dataSourceManager.loadByBatchUid(selectedDataSourceKey.trim());
                                    } catch (PersistenceException e) {
                                        logger.error("Method: persistEnrollmentsByDAO: PersistenceException: " + e.getFullMessageTrace());
                                    }

                                    loadedCourseMembership.setDataSourceId(dataSource.getId());
                                    outcome.setDataSourceKey(selectedDataSourceKey.trim());
                                }
                            }

                            if (form.getIsCourseRoleRequired() && StringUtil.notEmpty(selectedCourseRole=form.getSelectedCourseRole())) {
                                CourseMembership.Role role = CourseMembership.Role.fromFieldName(selectedCourseRole.trim());
                                loadedCourseMembership.setRole(role);
                                setPersistenceOutcomeCourseRole(outcome, role, enrolmentType);
                            }

                            setPersistenceOutcomeCourseRole(outcome, loadedCourseMembership.getRole(), enrolmentType);
                            courseMembershipDAO.update(loadedCourseMembership);
                            outcome.setIsPersistedSuccess(true);
                            log(outcome);
                        }
                    }
                }
                outcomes.add(outcome);
            }
        }
        return outcomes;
    }

    private Enrollment loadEnrolment(String idString) {
        Id id = null;
        try {
            id = Id.generateId(Enrollment.DATA_TYPE, idString.trim());
        } catch (PersistenceException e) {
            logger.error("Method: loadEnrolment: PersistenceException: " + e.getMessage());
        }
        if (null != id) {
            Enrollment enrollment = new Enrollment();
            enrollment.setId(id);
            List<Enrollment> loadedEnrollments = null;
            try {
                loadedEnrollments = enrollmentLoader.load(enrollment);
            } catch (PersistenceException e) {
                logger.error("Method: loadEnrolment: PersistenceException: " + e.getMessage());
            }

            if (null != loadedEnrollments && loadedEnrollments.size() == 1) {
                return loadedEnrollments.get(0);
            }
        }
        return null;
    }

    public List<PersistenceOutcome> persistEnrolments(String[] selectedEnrollmentIds, Form form, String enrolmentType) {
        List<PersistenceOutcome> outcomes = null;
        if (null != form && form.getIsUpdateRequired() && null != selectedEnrollmentIds && selectedEnrollmentIds.length > 0) {
            logger.info(Parameter.CHANGE_COMMENTS_TEXTAREA_KEY.getName() + ": " + form.getChangeComments());
            outcomes = new ArrayList<PersistenceOutcome>();
            PersistenceOutcome outcome = null;
            boolean isHeaderLogged = false;
            for (String idString : selectedEnrollmentIds) {
                if (StringUtil.notEmpty(idString)) {
                    Id id = null;
                    try {
                        id = Id.generateId(Enrollment.DATA_TYPE, idString.trim());
                    } catch (PersistenceException e) {
                        logger.error("Method: persistEnrollments: PersistenceException: " + e.getMessage());
                    }
                    if (null != id) {
                        Enrollment enrollment = new Enrollment();
                        enrollment.setId(id);
                        List<Enrollment> loadedEnrollments = null;
                        try {
                            loadedEnrollments = enrollmentLoader.load(enrollment);
                        } catch (PersistenceException e) {
                            logger.error("Method: persistEnrollments: PersistenceException: " + e.getMessage());
                        }
                        if (null != loadedEnrollments && loadedEnrollments.size() == 1) {
                            Enrollment loadedEnrollment = loadedEnrollments.get(0);
                            // record status before update
                            outcomes.add(getPersistenceOutcome(loadedEnrollment, enrolmentType, true));

                            // outcome after update
                            outcome = new PersistenceOutcome();
                            if (!isHeaderLogged) {
                                logger.info(outcome.getHeader());
                                isHeaderLogged = true;
                            }
                            log(loadedEnrollment, enrolmentType);

                            outcome.setUserBatchUid(loadedEnrollment.getPersonBatchUid());
                            outcome.setCourseBatchUid(loadedEnrollment.getCourseSiteBatchUid());
                            outcome.setDataSourceKey(loadedEnrollment.getDataSourceBatchUid());
                            outcome.setRowStatus(loadedEnrollment.getRowStatus().toFieldName());
                            outcome.setIsAvailable(loadedEnrollment.getIsAvailable());

                            String selectedAvailability = null, selectedRowStatus = null, selectedDataSourceKey = null;


                            if (form.getIsAvailabilityUpdateRequired()) {
                                if (StringUtil.notEmpty(selectedAvailability = form.getSelectedAvailability())
                                        && selectedAvailability.trim().equals(B2Enum.Available.getName())) {
                                    outcome.setIsAvailable(true);
                                    loadedEnrollment.setIsAvailable(true);
                                } else {
                                    outcome.setIsAvailable(false);
                                    loadedEnrollment.setIsAvailable(false);
                                }
                            }

                            if (form.getIsStatusUpdateRequired()) {
                                if (StringUtil.notEmpty(selectedRowStatus = form.getSelectedRowStatus())
                                        && selectedRowStatus.trim().equals(B2Enum.Enabled.getName())) {
                                    outcome.setRowStatus(RowStatus.ENABLED.toFieldName());
                                    loadedEnrollment.setRowStatus(RowStatus.ENABLED);
                                } else {
                                    outcome.setRowStatus(RowStatus.DISABLED.toFieldName());
                                    loadedEnrollment.setRowStatus(RowStatus.DISABLED);
                                }
                            }

                            if (form.getIsDataSourceKeyUpdateRequired()) {
                                if (StringUtil.notEmpty(selectedDataSourceKey = form.getSelectedDataSourceKey())) {
                                    loadedEnrollment.setDataSourceBatchUid(selectedDataSourceKey.trim());
                                    outcome.setDataSourceKey(selectedDataSourceKey.trim());
                                }
                            }

                            String errorMessage = null;
                            try {

                                setPersistenceOutcomeCourseRole(outcome, loadedEnrollment.getRole(), enrolmentType);
                                enrollmentPersister.save(loadedEnrollment);
                                outcome.setIsPersistedSuccess(true);

                                //To persist Course Role other than Guess or Student
                                if (form.getIsCourseRoleRequired()) {
                                    outcome.setIsPersistedSuccess(persistCourseMembership(outcome, loadedEnrollment, form.getSelectedCourseRole(), enrolmentType));
                                }
                                log(outcome);
                            } catch (PersistenceException e) {
                                errorMessage = "PersistenceException while updating enrolment:"
                                        + " enrolment_id: " + loadedEnrollment.getId().getExternalString()
                                        + " : course_id: " + loadedEnrollment.getCourseId().getExternalString()
                                        + " : user_id: " + loadedEnrollment.getUserId().getExternalString()
                                        + " : " + e.getMessage();
                                outcome.setErrorMessage(errorMessage);
                                logger.error("Method: persistEnrollments: "+ errorMessage);
                            } catch (ConstraintViolationException e) {
                                errorMessage = "ConstraintViolationException while updating enrolment:"
                                        + " enrolment_id: " + loadedEnrollment.getId().getExternalString()
                                        + " : course_id: " + loadedEnrollment.getCourseId().getExternalString()
                                        + " : user_id: " + loadedEnrollment.getUserId().getExternalString()
                                        + " : " + e.getMessage();
                                outcome.setErrorMessage(errorMessage);
                                logger.error("Method: persistEnrollments: "+ errorMessage);
                            } catch (ValidationException e) {
                                errorMessage = "ValidationException while updating enrolment:"
                                        + " enrolment_id: " + loadedEnrollment.getId().getExternalString()
                                        + " : course_id: " + loadedEnrollment.getCourseId().getExternalString()
                                        + " : user_id: " + loadedEnrollment.getUserId().getExternalString()
                                        + " : " + e.getMessage();
                                outcome.setErrorMessage(errorMessage);
                                logger.error("Method: persistEnrollments: "+ errorMessage);
                            }
                        }
                    }

                }
                outcomes.add(outcome);
            }
        }
        return outcomes;
    }

    // To allow course role other than Guest or Student be persisted.
    // EnrollmentPersister only allow Student and Guest Roles.
    private boolean persistCourseMembership(PersistenceOutcome outcome, Enrollment enrolment, String selectedCourseRole, String enrolmentType) {
        if (StringUtil.notEmpty(selectedCourseRole) && null != enrolment) {
            String errorMessage = null;
            CourseMembership.Role role = CourseMembership.Role.fromFieldName(selectedCourseRole.trim());
            setPersistenceOutcomeCourseRole(outcome, role, enrolmentType);
            CourseMembership courseMembership = null;

            try {
                Id id = Id.generateId(CourseMembership.DATA_TYPE, enrolment.getId().getExternalString());
                CourseMembershipDbLoader courseMembershipLoader = CourseMembershipDbLoader.Default.getInstance();
                courseMembership = courseMembershipLoader.loadById(id);
                // The following sometime fails to loag the coursemembership object. Use loadById instead
                //courseMembership = courseMembershipLoader.loadByCourseAndUserId(enrolment.getCourseId(), enrolment.getUserId());
                courseMembership.setRole(role);
            } catch (PersistenceException e) {
                errorMessage = "PersistenceException: unable to load course membership for:"
                        + " course_id: " + enrolment.getCourseId().getExternalString()
                        + " : user_id: " + enrolment.getUserId().getExternalString()
                        + " : " + e.getMessage();
                outcome.setErrorMessage(errorMessage);
                logger.error("Method: persistCourseMembership: "+ errorMessage);

                return false;
            }

            try {
                CourseMembershipDbPersister courseMembershipPersister = CourseMembershipDbPersister.Default.getInstance();
                courseMembershipPersister.persist(courseMembership);
                return true;
            } catch (ValidationException e) {
                errorMessage = "ValidationException: "
                        + " course_id: " + courseMembership.getCourseId().getExternalString()
                        + " : user_id: " + courseMembership.getUserId().getExternalString()
                        + " : " + e.getMessage();
                outcome.setErrorMessage(errorMessage);
                logger.error("Method: persistCourseMembership: "+errorMessage);
            } catch (PersistenceException e) {
                errorMessage = "PersistenceException: "
                        + " course_id: " + courseMembership.getCourseId().getExternalString()
                        + " : user_id: " + courseMembership.getUserId().getExternalString()
                        + " : " + e.getMessage();
                outcome.setErrorMessage(errorMessage);
                logger.error("Method: persistCourseMembership: "+errorMessage);
            }
        }
        return false;
    }

    public List<B2Enrolment> getCourseEnrolments(String courseSiteId, String sortByPropertyName) {
        List<B2Enrolment> b2Enrolments = null;
        CourseSite cs = serviceManager.getCourseSiteByCourseSiteId(courseSiteId);

        String courseSiteBatchUid = null;
        if (cs != null && StringUtil.notEmpty(courseSiteBatchUid = cs.getBatchUid())) {
            List<Enrollment> enrollments = getCourseEnrolmentListByBatchUid(courseSiteBatchUid);

            if (enrollments.size() > 10000) {
                logger.info("Method: getCourseEnrolments: Enrolment size is: " + enrollments.size());
            }

            if (null != enrollments) {
                serviceManager.sortEnrolments(enrollments, sortByPropertyName);
                B2Enrolment b2Enrolment = null;
                b2Enrolments = new ArrayList<B2Enrolment>();
                for (Enrollment enrollment : enrollments) {
                    b2Enrolment = new B2Enrolment();
                    b2Enrolment.setEnrollment(enrollment);
                    b2Enrolment.setServiceLevel(cs.getServiceLevelType().getFieldName());
                    b2Enrolment.setRole(getCourseOrOrganisationRole(courseRolesMap, enrollment.getRole()));
                    try {
                        Person person = personLoader.load(enrollment.getPersonBatchUid());
                        b2Enrolment.setUserId(person.getUserName());
                        b2Enrolment.setGivenName(person.getGivenName());
                        b2Enrolment.setFamilyName(person.getFamilyName());
                    } catch (KeyNotFoundException e) {
                        logger.error("Method: getCourseEnrolments: KeyNotFoundException: " + e.getMessage());
                    } catch (PersistenceException e) {

                        logger.error("Method: getCourseEnrolments: PersistenceException: " + e.getMessage());
                        e.printStackTrace();
                    }
                    b2Enrolments.add(b2Enrolment);
                }
            }
        }
        return b2Enrolments;
    }

    public List<B2Enrolment> getOrganisationEnrolments(String courseSiteId, String sortByPropertyName) {
        List<B2Enrolment> b2Enrolments = null;

        Course course = getOrganisation(courseSiteId);
        if (course != null) {
            List<Enrollment> enrollments = getCourseEnrolmentListById(course.getId());
            if (enrollments.size() > 10000) {
                logger.info("Method: getOrganisationEnrolments: Enrolment size is: " + enrollments.size());
            }
            if (null != enrollments) {
                serviceManager.sortEnrolments(enrollments, sortByPropertyName);
                B2Enrolment b2Enrolment = null;
                b2Enrolments = new ArrayList<B2Enrolment>();
                for (Enrollment enrollment : enrollments) {
                    b2Enrolment = new B2Enrolment();
                    b2Enrolment.setEnrollment(enrollment);
                    b2Enrolment.setServiceLevel(course.getServiceLevelType().getFieldName());
                    b2Enrolment.setRole(getCourseOrOrganisationRole(organisationRolesMap, enrollment.getRole()));
                    try {
                        Person person = personLoader.load(enrollment.getPersonBatchUid());

                        b2Enrolment.setUserId(person.getUserName());
                        b2Enrolment.setGivenName(person.getGivenName());
                        b2Enrolment.setFamilyName(person.getFamilyName());
                    } catch (KeyNotFoundException e) {
                        logger.error("Method: getOrganisationEnrolments: KeyNotFoundException: " + e.getMessage());
                    } catch (PersistenceException e) {
                        logger.error("Method: getOrganisationEnrolments: PersistenceException: " + e.getMessage());
                    }
                    b2Enrolments.add(b2Enrolment);
                }
            }
        }
        return b2Enrolments;
    }

    private List<Enrollment> getCourseEnrolmentListByBatchUid(String courseSiteBatchUid) {
        CourseSite cs = serviceManager.getCourseSiteByBatchUid(courseSiteBatchUid);
        if (null != cs) {
            Enrollment enrollment = new Enrollment();
            enrollment.setCourseSiteBatchUid(cs.getBatchUid());
            try {
                return enrollmentLoader.load(enrollment);
            } catch (PersistenceException e) {
                logger.error("Method: getCourseEnrolmentListByBatchUid: PersistenceException: " + e.getMessage());
            }
        }
        return null;
    }

    private List<Enrollment> getCourseEnrolmentListById(Id courseId) {
        if (null != courseId) {
            Enrollment enrollment = new Enrollment();
            enrollment.setCourseId(courseId);
            try {
                return enrollmentLoader.load(enrollment);
            } catch (PersistenceException e) {
                logger.error("Method: getCourseEnrolmentListById: PersistenceException: " + e.getMessage());
            }
        }
        return null;
    }


    private Course getOrganisation(String organisationIdString) {
        try {

            CourseSearch courseSearch = CourseSearch.getInfoSearch(CourseSearch.SearchKey.CourseId,
                    SearchOperator.Equals, organisationIdString, ServiceLevel.COMMUNITY);

            // Using courseDbLoader.loadByCourseSearch with ServiceLevel.COMMUNITY
            // allows loading disabled ORGANIZATIONS or COURSES, unfortunately the result courses or
            // organizations do not include batchUid. This means it requires to further use
            // courseSiteLoader.load with a template CourseSite to load the disabled courses or organizations
            // so that their enrolments can be loaded later on.
            //
            // Using courseDbLoader.loadByCourseSearch also allows loading disabled ORGANIZATIONS or COURSES
            // with case insensitive course or organization id string.
            List<Course> courses = courseDbLoader.loadByCourseSearch(courseSearch);
            if (null != courses && courses.size() == 1) {
                return courses.get(0);
            }
        } catch (KeyNotFoundException e1) {
            logger.error("Method: getCourseEnrolments: KeyNotFoundException: " + e1.getMessage());
        } catch (PersistenceException e1) {
            logger.error("Method: getCourseEnrolments: PersistenceException: " + e1.getMessage());
        }
        return null;
    }


    private List<CourseCategoryMembership> getCourseCategoryMemberships(String categoryBatchUid) {
        if (StringUtil.notEmpty(categoryBatchUid)) {
            CourseCategoryMembership courseCategoryMembership = new CourseCategoryMembership();
            courseCategoryMembership.setCategoryBatchUid(categoryBatchUid.trim());
            try {
                return courseCategoryMembershipLoader.load(courseCategoryMembership);
            } catch (PersistenceException e) {
                logger.error("Method: getCourseCategoryMemberships: " + e.getMessage());
            }
        }
        return null;
    }

    private List<OrganizationCategoryMembership> getOrganisationCategoryMemberships(String categoryBatchUid) {
        if (StringUtil.notEmpty(categoryBatchUid)) {
            OrganizationCategoryMembership organizationCategoryMembership = new OrganizationCategoryMembership();
            organizationCategoryMembership.setCategoryBatchUid(categoryBatchUid.trim());
            try {
                return organizationCategoryMembershipLoader.load(organizationCategoryMembership);
            } catch (PersistenceException e) {
                logger.error("Method: getOrganisationCategoryMemberships: " + e.getMessage());
            }
        }
        return null;
    }


    public List<B2Enrolment> getUserEnrolments(Form form, String sortByPropertyName) {
        List<B2Enrolment> enrolments = null;
        String selectedUserName = null;
        if (null != form && StringUtil.notEmpty(selectedUserName = form.getSearchText())) {
            if (StringUtil.notEmpty(selectedUserName = form.getSearchText())) {
                List<B2Enrolment> courseEnrolments = null;
                List<B2Enrolment> organisationEnrolments = null;
                String selectedCategoryBatchUid = form.getCategory();
                String selectedOrganisationCategoryBatchUid = form.getOrganisationCategory();
                // Get User Enrolments in a selected Course and/or Organisation Category
                if (StringUtil.notEmpty(selectedCategoryBatchUid) && !selectedCategoryBatchUid.equals("-1")) {
                    // Sort by courseBatchUid
                    courseEnrolments = getUserEnrolmentsInCategory(selectedUserName, selectedCategoryBatchUid, sortByPropertyName);
                }
                if (StringUtil.notEmpty(selectedOrganisationCategoryBatchUid) && !selectedOrganisationCategoryBatchUid.equals("-1")) {
                    organisationEnrolments = getUserEnrolmentsInOrganisationCategory(selectedUserName, selectedOrganisationCategoryBatchUid, sortByPropertyName);
                }
                if (null != courseEnrolments || null != organisationEnrolments) {
                    enrolments = new ArrayList<B2Enrolment>();
                    if (null != courseEnrolments) {
                        enrolments.addAll(courseEnrolments);
                    }
                    if (null != organisationEnrolments) {
                        enrolments.addAll(organisationEnrolments);
                    }
                } else {
                    enrolments = getUserEnrolments(selectedUserName, sortByPropertyName);
                }
            }
        }

        if (null != enrolments) {
            return Collections.unmodifiableList(enrolments);
        }

        return null;
    }


    private List<B2Enrolment> getUserEnrolments(String userName, String sortByPropertyName) {
        List<B2Enrolment> b2Enrolments = null;
        List<B2Enrolment> b2CourseEnrolments = null;
        List<B2Enrolment> b2OrganisationEnrolments = null;
        List<Enrollment> enrollments = getUserEnrolmentList(userName);
        if (null != enrollments) {
            serviceManager.sortEnrolments(enrollments, sortByPropertyName);
            B2Enrolment b2Enrolment = null;
            b2Enrolments = new ArrayList<B2Enrolment>();
            b2CourseEnrolments = new ArrayList<B2Enrolment>();
            b2OrganisationEnrolments = new ArrayList<B2Enrolment>();
            for (Enrollment enrollment : enrollments) {
                b2Enrolment = new B2Enrolment();
                b2Enrolment.setEnrollment(enrollment);

                CourseSite cs = serviceManager.getCourseSiteByBatchUid(enrollment.getCourseSiteBatchUid());
                if (null != cs) {
                    b2Enrolment.setCourseId(cs.getCourseId());
                    b2Enrolment.setCourseTitle(cs.getTitle());
                    b2Enrolment.setServiceLevel(cs.getServiceLevelType().getFieldName());
                }

                if (cs.getServiceLevelType().equals(ServiceLevel.COMMUNITY)) {
                    b2Enrolment.setRole(getCourseOrOrganisationRole(organisationRolesMap, enrollment.getRole()));
                    b2OrganisationEnrolments.add(b2Enrolment);
                } else {
                    b2Enrolment.setRole(getCourseOrOrganisationRole(courseRolesMap, enrollment.getRole()));
                    b2CourseEnrolments.add(b2Enrolment);
                }
            }

            if (null != b2CourseEnrolments) {
                b2Enrolments.addAll(b2CourseEnrolments);
            }
            if (null != b2OrganisationEnrolments) {
                b2Enrolments.addAll(b2OrganisationEnrolments);
            }
        }

        return b2Enrolments;
    }

    private List<Enrollment> getUserEnrolmentList(String userName) {
        Person loadedPerson = serviceManager.getPersonByUserName(userName);
        if (null != loadedPerson) {
            Enrollment enrollment = new Enrollment();
            enrollment.setPersonBatchUid(loadedPerson.getBatchUid());
            try {
                return enrollmentLoader.load(enrollment);
            } catch (PersistenceException e) {
                logger.error("Method: getUserEnrolmentList: PersistenceException: " + e.getMessage());
            }
        }
        return null;
    }

    private List<B2Enrolment> getUserEnrolmentsInCategory(String userName, String categoryBatchUid, String sortByPropertyName) {
        List<B2Enrolment> b2Enrolments = null;
        List<Enrollment> enrollments = getUserEnrolmentList(userName);
        List<CourseCategoryMembership> courseCategoryMemberships = getCourseCategoryMemberships(categoryBatchUid);

        if (null != enrollments && null != courseCategoryMemberships) {
            serviceManager.sortEnrolments(enrollments, sortByPropertyName);
            B2Enrolment b2Enrolment = null;
            b2Enrolments = new ArrayList<B2Enrolment>();
            String courseSiteBatchUid = null;
            for (Enrollment enrollment : enrollments) {
                b2Enrolment = new B2Enrolment();
                b2Enrolment.setEnrollment(enrollment);
                b2Enrolment.setRole(getCourseOrOrganisationRole(courseRolesMap, enrollment.getRole()));
                for (CourseCategoryMembership courseCategoryMembership : courseCategoryMemberships) {
                    if (StringUtil.notEmpty(courseSiteBatchUid = enrollment.getCourseSiteBatchUid()) &&
                            courseSiteBatchUid.equals(courseCategoryMembership.getCourseSiteBatchUid())) {
                        b2Enrolment.setCategory(courseCategoryMembership.getCategoryBatchUid());
                        CourseSite cs = serviceManager.getCourseSiteByBatchUid(courseSiteBatchUid);
                        if (null != cs) {
                            b2Enrolment.setCourseId(cs.getCourseId());
                            b2Enrolment.setCourseTitle(cs.getTitle());
                            b2Enrolment.setServiceLevel(cs.getServiceLevelType().getFieldName());
                        }

                        b2Enrolments.add(b2Enrolment);

                        break;
                    }
                }
            }
        }

        return b2Enrolments;
    }

    private List<B2Enrolment> getUserEnrolmentsInOrganisationCategory(String userName, String categoryBatchUid, String sortByPropertyName) {
        List<B2Enrolment> b2Enrolments = null;
        List<Enrollment> enrollments = getUserEnrolmentList(userName);
        List<OrganizationCategoryMembership> organisationCategoryMemberships = getOrganisationCategoryMemberships(categoryBatchUid);
        if (null != enrollments && null != organisationCategoryMemberships) {
            serviceManager.sortEnrolments(enrollments, sortByPropertyName);
            B2Enrolment b2Enrolment = null;
            b2Enrolments = new ArrayList<B2Enrolment>();
            String courseSiteBatchUid = null;
            for (Enrollment enrollment : enrollments) {
                b2Enrolment = new B2Enrolment();
                b2Enrolment.setEnrollment(enrollment);
                b2Enrolment.setRole(getCourseOrOrganisationRole(organisationRolesMap, enrollment.getRole()));
                for (OrganizationCategoryMembership organisationCategoryMembership : organisationCategoryMemberships) {
                    if (StringUtil.notEmpty(courseSiteBatchUid = enrollment.getCourseSiteBatchUid()) &&
                            courseSiteBatchUid.equals(organisationCategoryMembership.getOrganizationBatchUid())) {
                        b2Enrolment.setCategory(organisationCategoryMembership.getCategoryBatchUid());
                        CourseSite cs = serviceManager.getCourseSiteByBatchUid(courseSiteBatchUid);
                        if (null != cs) {
                            b2Enrolment.setCourseId(cs.getCourseId());
                            b2Enrolment.setCourseTitle(cs.getTitle());
                            b2Enrolment.setServiceLevel(cs.getServiceLevelType().getFieldName());
                        }
                        b2Enrolments.add(b2Enrolment);
                        break;
                    }
                }
            }
        }

        return b2Enrolments;
    }

    private String getCourseOrOrganisationRole(Map<CourseMembership.Role, String> rolesMap, CourseMembership.Role role) {
        String roleString = null;
        if (null != rolesMap && null != role) {
            roleString = rolesMap.get(role);
            if (StringUtil.isEmpty(roleString)) {
                roleString = role.toExternalString();
            }

        } else if (null != role) {
            roleString = role.toExternalString();
        }
        return roleString;
    }

    private void log(Enrollment enrolment, String enrolmentType) {
        PersistenceOutcome origin = getPersistenceOutcome(enrolment, enrolmentType, true);
        if (null != origin) {
            logger.info("ORIGIN: " + origin.toString());
        }
    }

    private void log(PersistenceOutcome outcome) {
        if (null != outcome) {
            logger.info("OUTCOME: " + outcome.toString());
        }
    }

    private PersistenceOutcome getPersistenceOutcome(CourseMembership courseMembership, String enrolmentType, boolean isBeforeUpdate) {
        if (null != courseMembership) {
            PersistenceOutcome outcome = new PersistenceOutcome();
            outcome.setUserBatchUid(courseMembership.getUser().getBatchUid());
            //outcome.setCourseBatchUid(courseMembership.getCourse);
            //outcome.setDataSourceKey(courseMembership);

            //outcome.setRowStatus(courseMembership.getRowStatus().toFieldName());
            //outcome.setDataSourceKey(courseMembership.getDataSourceBatchUid());
            outcome.setIsAvailable(courseMembership.getIsAvailable());

            setPersistenceOutcomeCourseRole(outcome, courseMembership.getRole(), enrolmentType);

            if (isBeforeUpdate) {
                outcome.setIsBeforeUpdate(isBeforeUpdate);
            }
            return outcome;
        }
        return null;
    }

    private PersistenceOutcome getPersistenceOutcome(Enrollment enrolment, String enrolmentType, boolean isBeforeUpdate) {
        if (null != enrolment) {
            PersistenceOutcome outcome = new PersistenceOutcome();
            outcome.setUserBatchUid(enrolment.getPersonBatchUid());
            outcome.setCourseBatchUid(enrolment.getCourseSiteBatchUid());
            outcome.setDataSourceKey(enrolment.getDataSourceBatchUid());

            outcome.setRowStatus(enrolment.getRowStatus().toFieldName());
            outcome.setDataSourceKey(enrolment.getDataSourceBatchUid());
            outcome.setIsAvailable(enrolment.getIsAvailable());

            setPersistenceOutcomeCourseRole(outcome, enrolment.getRole(), enrolmentType);

            if (isBeforeUpdate) {
                outcome.setIsBeforeUpdate(isBeforeUpdate);
            }
            return outcome;
        }
        return null;
    }

    private void setPersistenceOutcomeCourseRole(PersistenceOutcome outcome, CourseMembership.Role role, String enrolmentType) {
        if (null != outcome &&
                null != role &&
                StringUtil.notEmpty(enrolmentType)
                && enrolmentType.equals(B2Enrolment.ORGANISATION_ENROLMENT_TYPE)) {
            outcome.setCourseRole(getCourseOrOrganisationRole(organisationRolesMap, role));
        } else {
            outcome.setCourseRole(getCourseOrOrganisationRole(courseRolesMap, role));
        }
    }
}
