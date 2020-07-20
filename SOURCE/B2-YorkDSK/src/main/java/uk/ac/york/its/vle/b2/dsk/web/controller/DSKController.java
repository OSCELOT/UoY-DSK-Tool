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
package uk.ac.york.its.vle.b2.dsk.web.controller;

/**
 * @author Kelvin Hai {@link <a href="mailto:kelvin.hai@york.ac.uk">kelvin.hai@york.ac.uk</a>}
 * @version $Revision$ $Date$
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;

import blackboard.admin.data.datasource.DataSource;
import blackboard.data.course.CourseMembership;
import blackboard.util.StringUtil;


import uk.ac.york.its.vle.b2.dsk.data.*;

import uk.ac.york.its.vle.b2.dsk.service.ReferenceServiceManager;
import uk.ac.york.its.vle.b2.dsk.service.ServiceManager;

public class DSKController {
    private static final Logger logger = LoggerFactory.getLogger(DSKController.class);

    @Autowired
    protected ServiceManager serviceManager;

    @Autowired
    protected ReferenceServiceManager referenceServiceManager;

    @Resource
    protected Map<String, String> modelAndSessionAttributesMap;

    @ModelAttribute("courseRoles")
    public List<GenericFormSelectOption> populateCourseRoles(HttpServletRequest request) {
        return referenceServiceManager.getFilteredCourseRoles(request);
    }

    @ModelAttribute("organisationRoles")
    public List<GenericFormSelectOption> populateOrganisationRoles(HttpServletRequest request) {
        return referenceServiceManager.getFilteredOrganisationRoles(request);
    }

    @ModelAttribute("courseOrOrganisationRoles")
    public List<GenericFormSelectOption> populateCourseOrOrganisationRoles(HttpServletRequest request) {
        return referenceServiceManager.getCourseOrOrganisationRoles();
    }


    @ModelAttribute("userSearchKeys") // Form Reference Object
    public List<GenericFormSelectOption> populateUserSearchKeys() {
        return referenceServiceManager.getUserSearchKeys();
    }

    @ModelAttribute("userSearchOperators") // Form Reference Object
    public List<GenericFormSelectOption> populateUserSearchOperators() {
        return referenceServiceManager.getUserSearchOperators();
    }

    @ModelAttribute("courseSearchKeys") // Form Reference Object
    public List<GenericFormSelectOption> populateCourseSearchKeys() {
        return referenceServiceManager.getCourseSearchKeys();
    }


    @ModelAttribute("organisationSearchKeys") // Form Reference Object
    public List<GenericFormSelectOption> populateOraganisationSearchKeys() {
        return referenceServiceManager.getOrganisationSearchKeys();
    }


    @ModelAttribute("courseSearchOperators") // Form Reference Object
    public List<GenericFormSelectOption> populateCourseSearchOperators() {
        return referenceServiceManager.getCourseSearchOperators();
    }

    @ModelAttribute("categories") // Form Reference Object
    public List<GenericFormSelectOption> populateCategories() {
        return referenceServiceManager.getCourseCategories();
    }


    @ModelAttribute("organisationCategories") // Form Reference Object
    public List<GenericFormSelectOption> populateOrganisationCategories() {
        return referenceServiceManager.getOrganisationCategories();
    }

    @ModelAttribute("dateOperators") // Form Reference Object
    public List<GenericFormSelectOption> populateDateSearchOperators() {
        return referenceServiceManager.getDateSearchOperators();
    }

    @ModelAttribute("userDataSourceKeys") // Form Reference Object
    public List<DataSource> populateUserDataSourceKeys() {
        return referenceServiceManager.getUserDataSourceKeys();
    }

    @ModelAttribute("dataSourceKeys") // Form Reference Object
    public List<DataSource> populateDataSourceKeys() {
        return referenceServiceManager.getDataSourceKeys();
    }

    @ModelAttribute("courseDataSourceKeys") // Form Reference Object
    public List<DataSource> populateCourseDataSourceKeys() {
        return referenceServiceManager.getCourseDataSourceKeys();
    }

    @ModelAttribute("organisationDataSourceKeys") // Form Reference Object
    public List<DataSource> populateOrganisationDataSourceKeys() {
        return referenceServiceManager.getOrganisationDataSourceKeys();
    }

    @ModelAttribute("enrolmentDataSourceKeys") // Form Reference Object
    public List<DataSource> populateEnrolmentDataSourceKeys() {
        return referenceServiceManager.getEnrolmentDataSourceKeys();
    }

    @ModelAttribute("userEnrolmentDataSourceKeys") // Form Reference Object
    public List<DataSource> populateUserEnrolmentDataSourceKeys() {
        return referenceServiceManager.getUserEnrolmentDataSourceKeys();
    }

    @ModelAttribute("courseEnrolmentDataSourceKeys") // Form Reference Object
    public List<DataSource> populateCourseEnrolmentDataSourceKeys() {
        return referenceServiceManager.getCourseEnrolmentDataSourceKeys();
    }

    protected void populate(ModelMap model, Form form, String recallUrl) {
        if (model != null && form != null) {
            model.addAttribute("receiptType", B2Enum.RECEIPT_SUCCESS_TYPE.getName());
            model.addAttribute("isUpdateRequired", form.getIsUpdateRequired());
            model.addAttribute("selectedCourseRole", form.getSelectedCourseRole());
            model.addAttribute("selectedRowStatus", form.getSelectedRowStatus());
            model.addAttribute("selectedAvailability", form.getSelectedAvailability());
            model.addAttribute("selectedDataSourceKey", form.getSelectedDataSourceKey());
            model.addAttribute(modelAndSessionAttributesMap.get("b2.model.key.recall_url"), recallUrl);
        }
    }

    protected boolean isFormReadyToPersist(Form form) {
        if (null != form
                && form.getIsUpdateRequired() == true
                && (form.getIsCourseRoleRequired()
                || form.getIsAvailabilityUpdateRequired()
                || form.getIsStatusUpdateRequired()
                || form.getIsDataSourceKeyUpdateRequired())) {
            return true;
        }
        return false;
    }

    protected void setForm(ModelMap model) {
        model.addAttribute(new Form());
    }

    protected void setForm(ModelMap model, HttpServletRequest request) {
        Form form = new Form();

        model.addAttribute(form);
    }

    protected Calendar getCalendar(HttpServletRequest request) {
        String bbDateTimePickerCalendarString = null;
        try {
            //Bb datePicker parameters default names
            //Param bbDateTimePicker_datetime is: 2013-7-30 12:37:00
            //Param bbDateTimePicker_date is: 30/07/2013
            //bbDateTimePickerCalendarString=request.getParameter("bbDateTimePicker_datetime");

            bbDateTimePickerCalendarString = request.getParameter(modelAndSessionAttributesMap.get("bb.date.picker.key.date"));
        } catch (NullPointerException e) {
        }
        return getCalendar(bbDateTimePickerCalendarString);
    }

    protected Calendar getCalendar(String dateString) {
        String dateFormat = "dd/MM/yyyy";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date = null;
        try {
            date = sdf.parse(dateString);
            cal.setTime(date);
        } catch (ParseException e) {
            logger.error("Method: getCalendar: " + e.getMessage());
        }
        return cal;
    }


    protected ModelMap getGetModel(ModelMap model, HttpServletRequest request, String checkBoxKey) {

        Form form = null;
        if (null != model && null != request) {
            String queryString = null;
            if (StringUtil.notEmpty(queryString = request.getQueryString())
                    && queryString.matches(".*sortDir.*")
                    && queryString.matches(".*sortCol.*")) {

                Object object = null;

                if (null != (object = request.getSession().getAttribute(modelAndSessionAttributesMap.get("b2.session.key.users")))) {
                    model.addAttribute(modelAndSessionAttributesMap.get("b2.model.key.users"), object);
                }

                if (null != (object = request.getSession().getAttribute(modelAndSessionAttributesMap.get("b2.session.key.courses")))) {
                    model.addAttribute(modelAndSessionAttributesMap.get("b2.model.key.courses"), object);
                }

                if (null != (object = request.getSession().getAttribute(modelAndSessionAttributesMap.get("b2.session.key.organisations")))) {
                    model.addAttribute(modelAndSessionAttributesMap.get("b2.model.key.organisations"), object);
                }

                if (null != (object = request.getSession().getAttribute(modelAndSessionAttributesMap.get("b2.session.key.user_enrolments")))) {
                    model.addAttribute(modelAndSessionAttributesMap.get("b2.model.key.user_enrolments"), object);
                }

                if (null != (object = request.getSession().getAttribute(modelAndSessionAttributesMap.get("b2.session.key.organisation_enrolments")))) {
                    model.addAttribute(modelAndSessionAttributesMap.get("b2.model.key.organisation_enrolments"), object);
                }

                if (null != (object = request.getSession().getAttribute(modelAndSessionAttributesMap.get("b2.session.key.course_enrolments")))) {
                    model.addAttribute(modelAndSessionAttributesMap.get("b2.model.key.course_enrolments"), object);
                }

                if (null != (object = request.getSession().getAttribute(modelAndSessionAttributesMap.get("b2.session.key.form")))) {
                    form = (Form) object;
                }
            }


            // Set parameter name for the multiple checkboxes in HTML form
            if (StringUtil.notEmpty(checkBoxKey)) {
                model.addAttribute(modelAndSessionAttributesMap.get("b2.model.key.checkbox"), checkBoxKey);
            }


            //This parameter will be passed in by the Bb inventory list when select
            // all elements box has been checked.
            // Pass this parameter back to the HTML form as a hidden variable when the form
            // is submitted by the Post method.
            String selectAllMode = null;


            if (null != (selectAllMode = request.getParameter(modelAndSessionAttributesMap.get("bb.inventory.select.all.mode.key")))) {
                model.addAttribute(modelAndSessionAttributesMap.get("bb.inventory.select.all.mode.key"), selectAllMode);
            }

        }

        if (null == form) {
            form = new Form();
        }

        form.setRequestURI(request.getRequestURI());
        model.addAttribute(form);
        model.addAttribute(modelAndSessionAttributesMap.get("b2.model.key.textarea.change.comments"), Parameter.CHANGE_COMMENTS_TEXTAREA_KEY.getName());
        return model;
    }


    protected ModelMap getPostModel(ModelMap model, HttpServletRequest request, String checkBoxKey) {

        Object object = null;

        if (null != (object = model.get(modelAndSessionAttributesMap.get("b2.model.key.form")))) {
            request.getSession().setAttribute(modelAndSessionAttributesMap.get("b2.session.key.form"), object);
        }

        if (null != (object = model.get(modelAndSessionAttributesMap.get("b2.model.key.organisation_enrolments")))) {
            model.remove(modelAndSessionAttributesMap.get("b2.model.key.organisations"));
            request.getSession().setAttribute(modelAndSessionAttributesMap.get("b2.session.key.organisation_enrolments"), object);
        }


        if (null != (object = model.get(modelAndSessionAttributesMap.get("b2.model.key.user_enrolments")))) {
            model.remove(modelAndSessionAttributesMap.get("b2.model.key.users"));
            request.getSession().setAttribute(modelAndSessionAttributesMap.get("b2.session.key.user_enrolments"), object);
        }


        if (null != (object = model.get(modelAndSessionAttributesMap.get("b2.model.key.course_enrolments")))) {
            model.remove(modelAndSessionAttributesMap.get("b2.model.key.courses"));
            request.getSession().setAttribute(modelAndSessionAttributesMap.get("b2.session.key.course_enrolments"), object);
        }


        if (null != (object = model.get(modelAndSessionAttributesMap.get("b2.model.key.users")))) {
            request.getSession().setAttribute(modelAndSessionAttributesMap.get("b2.session.key.users"), object);
        }


        if (null != (object = model.get(modelAndSessionAttributesMap.get("b2.model.key.courses")))) {
            request.getSession().setAttribute(modelAndSessionAttributesMap.get("b2.session.key.courses"), object);
        }

        if (null != (object = model.get(modelAndSessionAttributesMap.get("b2.model.key.organisations")))) {
            request.getSession().setAttribute(modelAndSessionAttributesMap.get("b2.session.key.organisations"), object);
        }

        // Set parameter name for the multiple checkboxes in HTML form
        if (StringUtil.notEmpty(checkBoxKey)) {
            model.addAttribute(modelAndSessionAttributesMap.get("b2.model.key.checkbox"), checkBoxKey);
        }

        model.addAttribute(modelAndSessionAttributesMap.get("b2.model.key.textarea.change.comments"), Parameter.CHANGE_COMMENTS_TEXTAREA_KEY.getName());

        return model;
    }


    protected void clearSessionAttributes(HttpServletRequest request) {
        request.getSession().removeAttribute(modelAndSessionAttributesMap.get("b2.session.key.form"));
        request.getSession().removeAttribute(modelAndSessionAttributesMap.get("b2.session.key.users"));
        request.getSession().removeAttribute(modelAndSessionAttributesMap.get("b2.session.key.courses"));
        request.getSession().removeAttribute(modelAndSessionAttributesMap.get("b2.session.key.organisations"));
        request.getSession().removeAttribute(modelAndSessionAttributesMap.get("b2.session.key.course_enrolments"));
        request.getSession().removeAttribute(modelAndSessionAttributesMap.get("b2.session.key.user_enrolments"));
        request.getSession().removeAttribute(modelAndSessionAttributesMap.get("b2.session.key.organisation_enrolments"));
    }

    protected void setUpdateError(ModelMap model, HttpServletRequest request, boolean isFormReady, boolean isRecordSelected) {
        if ((isRecordSelected && !isFormReady)
                || (!isRecordSelected && isFormReady)
                || (request.getParameter("go_submit_button") == null && !isFormReady && !isRecordSelected)
                ) {
            model.addAttribute("errorCode", "error.form.validation.failure");
        }
    }


    protected boolean isSelectedDataSourceKeyValid(ModelMap model, Form form, Map<String, String> fileteredDataSourceKeysMap) {
        //fileteredDataSourceKeysMap: key = idString, value = batchUid; e.g. key=_2_1, value=SYSTEM
        boolean isSelectedValid = true;
        if (form.getIsDataSourceKeyUpdateRequired()) {
            isSelectedValid = false;
            String selectedDataSourceKey = form.getSelectedDataSourceKey();
            if (StringUtil.notEmpty(selectedDataSourceKey)
                    && null != fileteredDataSourceKeysMap
                    && fileteredDataSourceKeysMap.containsValue(selectedDataSourceKey)) {
                isSelectedValid = true;
            } else {
                logger.error("Method: isSelectedDataSourceKeyValid : invalid data source key sepecifed: " + selectedDataSourceKey);
                model.addAttribute("errorCode", "error.form.validation.invalid_data_source_key_selected");
            }
        }
        return isSelectedValid;
    }

    protected boolean isSelectedRoleValid(ModelMap model, Form form, Map<CourseMembership.Role, String> fileteredRolesMap) {
        boolean isSelectedValid = true;
        if (form.getIsCourseRoleRequired()) {
            isSelectedValid = false;
            String selectedRole = form.getSelectedCourseRole();
            if (StringUtil.notEmpty(selectedRole)
                    && null != fileteredRolesMap
                    && fileteredRolesMap.containsKey(CourseMembership.Role.fromFieldName(selectedRole))) {
                isSelectedValid = true;
            } else {
                logger.error("Method: isSelectedRoleValid : invalid role sepecifed: " + selectedRole);
                model.addAttribute("errorCode", "error.form.validation.invalid_role_selected");
            }
        }
        return isSelectedValid;
    }

    protected void setEnrolmentComparators(ModelMap model) {
        Comparator<B2Enrolment> availabilityComparator =
                (B2Enrolment o1, B2Enrolment o2) -> Boolean.valueOf(o1.getEnrollment().getIsAvailable()).compareTo(Boolean.valueOf(o2.getEnrollment().getIsAvailable()));
        Comparator<B2Enrolment> rowStatusComparator =
                (B2Enrolment o1, B2Enrolment o2) -> o1.getEnrollment().getRowStatus().getFieldName().compareTo(o2.getEnrollment().getRowStatus().getFieldName());
        Comparator<B2Enrolment> dataSourceKeyComparator =
                (B2Enrolment o1, B2Enrolment o2) -> o1.getEnrollment().getDataSourceBatchUid().compareTo(o2.getEnrollment().getDataSourceBatchUid());
        Comparator<B2Enrolment> courseBatchUidComparator =
                (B2Enrolment o1, B2Enrolment o2) -> o1.getEnrollment().getCourseSiteBatchUid().compareTo(o2.getEnrollment().getCourseSiteBatchUid());
        model.addAttribute("availabilityComparator", availabilityComparator);
        model.addAttribute("rowStatusComparator", rowStatusComparator);
        model.addAttribute("dataSourceKeyComparator", dataSourceKeyComparator);
        model.addAttribute("courseBatchUidComparator", courseBatchUidComparator);
        model.addAttribute("categoryComparator", Comparator.comparing(B2Enrolment::getCategory));
        model.addAttribute("courseTypeComparator", Comparator.comparing(B2Enrolment::getServiceLevel));
        model.addAttribute("courseTitleComparator", Comparator.comparing(B2Enrolment::getCourseTitle));
        model.addAttribute("courseIdComparator", Comparator.comparing(B2Enrolment::getCourseId));
        model.addAttribute("roleComparator", Comparator.comparing(B2Enrolment::getRole));
        model.addAttribute("userNameComparator", Comparator.comparing(B2Enrolment::getUserId));
        model.addAttribute("givenNameComparator", Comparator.comparing(B2Enrolment::getGivenName));
        model.addAttribute("familyNameComparator", Comparator.comparing(B2Enrolment::getFamilyName));

        //model.addAttribute("dataSourceKeyComparator", Comparator.comparing(B2User::getDataSourceKey));
    }

    protected void setUserComparators(ModelMap model) {
        Comparator<B2User> availabilityComparator =
                (B2User o1, B2User o2) -> Boolean.valueOf(o1.getUser().getIsAvailable()).compareTo(Boolean.valueOf(o2.getUser().getIsAvailable()));
        Comparator<B2User> batUidComparator =
                (B2User o1, B2User o2) -> o1.getUser().getBatchUid().compareTo(o2.getUser().getBatchUid());
        Comparator<B2User> emailAddressComparator =
                (B2User o1, B2User o2) -> o1.getUser().getEmailAddress().compareTo(o2.getUser().getEmailAddress());
        Comparator<B2User> givenNameComparator =
                (B2User o1, B2User o2) -> o1.getUser().getGivenName().compareTo(o2.getUser().getGivenName());
        Comparator<B2User> familyNameComparator =
                (B2User o1, B2User o2) -> o1.getUser().getFamilyName().compareTo(o2.getUser().getFamilyName());
        model.addAttribute("availabilityComparator", availabilityComparator);
        model.addAttribute("batUidComparator", batUidComparator);
        model.addAttribute("givenNameComparator", givenNameComparator);
        model.addAttribute("familyNameComparator", familyNameComparator);
        model.addAttribute("emailAddressComparator", emailAddressComparator);
        model.addAttribute("rowStatusComparator", Comparator.comparing(B2User::getIsUserDisabled));
        model.addAttribute("userNameComparator", Comparator.comparing(B2User::getUserName));
        model.addAttribute("dataSourceKeyComparator", Comparator.comparing(B2User::getDataSourceKey));
    }

    protected void setCoureseComparators(ModelMap model) {
        // Comparators are created for the bbNG:inventoryList
        Comparator<B2Course> availabilityComparator =
                (B2Course o1, B2Course o2) ->
                        Boolean.valueOf(o1.getCourse().getIsAvailable()).compareTo(Boolean.valueOf(o2.getCourse().getIsAvailable()));
        Comparator<B2Course> courseTitleComparator =
                (B2Course o1, B2Course o2) ->
                        o1.getCourse().getTitle().compareTo(o2.getCourse().getTitle());
        Comparator<B2Course> courseTypeComparator =
                (B2Course o1, B2Course o2) ->
                        o1.getCourse().getServiceLevelType().getFieldName().compareTo(o2.getCourse().getServiceLevelType().getFieldName());
        model.addAttribute("availabilityComparator", availabilityComparator);
        model.addAttribute("courseTitleComparator", courseTitleComparator);
        model.addAttribute("courseTypeComparator", courseTypeComparator);
        model.addAttribute("courseIdComparator", Comparator.comparing(B2Course::getCourseId));
        model.addAttribute("batUidComparator", Comparator.comparing(B2Course::getCourseBatchUid));
        model.addAttribute("rowStatusComparator", Comparator.comparing(B2Course::getIsCourseDisabled));
        model.addAttribute("dataSourceKeyComparator", Comparator.comparing(B2Course::getDataSourceKey));
    }

    protected void setOrganisationComparators(ModelMap model) {
        setCoureseComparators(model);
    }

    protected String[] selectedCourseIds(List<B2Course> courses, String checkBoxKey, HttpServletRequest request) {
        String[] selectedCourseIds = null;
        try {
            selectedCourseIds = request.getParameterValues(checkBoxKey);
        } catch (NullPointerException e) {
            logger.error("Method: getSelectedCourseIds: " + e.getMessage());
        }

        //modelAndSessionAttributesMap is defined in blackboard.xml (selectAllMode=fromAllList)
        String selectAllMode = request.getParameter(modelAndSessionAttributesMap.get("bb.inventory.select.all.mode.key"));
        if (ArrayUtils.isEmpty(selectedCourseIds)
                && StringUtil.notEmpty(selectAllMode)
                && selectAllMode.equals(modelAndSessionAttributesMap.get("bb.inventory.select.all.mode.value"))
                && courses != null) {
            selectedCourseIds = new String[courses.size()];
            int index = 0;
            for (B2Course course : courses) {
                selectedCourseIds[index] = course.getCourseId();
                index++;
            }
        }

        return selectedCourseIds;
    }

    protected Map<String, String> getDataSourceKeysMap() {
        return referenceServiceManager.getDataSourceKeysMap();
    }

    protected Map<String, String> getUserDataSourceKeysMap() {
        return referenceServiceManager.getUserDataSourceKeysMap();
    }

    protected Map<String, String> getCourseDataSourceKeysMap() {
        return referenceServiceManager.getCourseDataSourceKeysMap();
    }

    protected Map<String, String> getOrganisationDataSourceKeysMap() {
        return referenceServiceManager.getOrganisationDataSourceKeysMap();
    }

    protected Map<String, String> getEnrolmentDataSourceKeysMap() {
        return referenceServiceManager.getEnrolmentDataSourceKeysMap();
    }

    protected Map<String, String> getUserEnrolmentDataSourceKeysMap() {
        return referenceServiceManager.getUserEnrolmentDataSourceKeysMap();
    }

    protected Map<String, String> getCourseEnrolmentDataSourceKeysMap() {
        return referenceServiceManager.getCourseEnrolmentDataSourceKeysMap();
    }

    protected Map<String, String> getCourseEnrolmentDataSourceKeys() {
        return referenceServiceManager.getCourseEnrolmentDataSourceKeysMap();
    }

    protected Map<CourseMembership.Role, String> getCourseRolesMap() {
        return referenceServiceManager.getCourseRolesMap();
    }

    protected Map<CourseMembership.Role, String> getOrganisationRolesMap() {
        return referenceServiceManager.getOrganisationRolesMap();
    }

    protected Map<CourseMembership.Role, String> getCourseOrOrganisationRolesMap() {
        return referenceServiceManager.getCourseOrOrganisationRolesMap();
    }
}
