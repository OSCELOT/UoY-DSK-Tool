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

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import uk.ac.york.its.vle.b2.dsk.data.B2Enrolment;
import uk.ac.york.its.vle.b2.dsk.data.Form;
import uk.ac.york.its.vle.b2.dsk.data.Parameter;
import uk.ac.york.its.vle.b2.dsk.model.PersistenceOutcome;
import uk.ac.york.its.vle.b2.dsk.service.EnrolmentDSKServiceManager;
import blackboard.data.course.CourseMembership;
import blackboard.util.StringUtil;


@Controller
@RequestMapping(value = "/enrolment")
@SessionAttributes(types = {Form.class})
public class EnrolmentDSKController extends DSKController {
    private static final Logger logger = LoggerFactory.getLogger(EnrolmentDSKController.class);
    private static final String DSK_TOOL_TYPE = "enrolment_dsk";

    @Autowired
    private EnrolmentDSKServiceManager enrolmentDskServiceManager;

    @RequestMapping(value = "{type}", method = RequestMethod.GET)
    public String showForm(@PathVariable String type, HttpServletRequest request, ModelMap model) {
        clearSessionAttributes(request);
        setForm(model);
        return this.getReturn(type, model);
    }

    @RequestMapping(value = "{type}/list", method = RequestMethod.GET)
    public String showList(@PathVariable String type, HttpServletRequest request, ModelMap model) {
        if (StringUtil.notEmpty(type)) {
            if (type.equals(B2Enrolment.COURSE_ENROLMENT_TYPE)) {
                getGetModel(model, request, Parameter.SELECTED_COURSE_ENROLMENTS_LIST.getName());
            } else if (type.equals(B2Enrolment.ORGANISATION_ENROLMENT_TYPE)) {
                getGetModel(model, request, Parameter.SELECTED_COURSE_ENROLMENTS_LIST.getName());
            } else if (type.equals(B2Enrolment.USER_ENROLMENT_TYPE)) {
                getGetModel(model, request, Parameter.SELECTED_USER_ENROLMENTS_LIST.getName());
            }
        }
        setEnrolmentComparators(model);
        return this.getReturn(type, model);
    }

    @RequestMapping(value = "{type}", method = RequestMethod.POST)
    public String processUEnrolmentSubmit(
            @PathVariable String type,
            HttpServletRequest request,
            @ModelAttribute Form form,
            BindingResult result,
            SessionStatus status,
            ModelMap model) {

        if (StringUtil.notEmpty(type)) {
            String[] selectedEnrolmentIds = null;
            List<B2Enrolment> enrolments = null;
            String checkBoxKey = null;
            Map<String, String> dataSourceKeysMap = null;
            Map<CourseMembership.Role, String> rolesMap = null;
            String returnString = null;
            String enrolmentType = B2Enrolment.COURSE_ENROLMENT_TYPE;
            if (type.equals(B2Enrolment.COURSE_ENROLMENT_TYPE)) {
                enrolmentType = B2Enrolment.COURSE_ENROLMENT_TYPE;
                checkBoxKey = Parameter.SELECTED_COURSE_ENROLMENTS_LIST.getName();

                dataSourceKeysMap = this.getCourseEnrolmentDataSourceKeysMap();
                rolesMap = this.getCourseRolesMap();

                String courseSiteId = form.getSearchText();
                if (StringUtil.notEmpty(courseSiteId)) {
                    returnString = "success_course_enrolment";
                    // sort by personBatchUid
                    model.addAttribute(
                            modelAndSessionAttributesMap.get("b2.model.key.course_enrolments"),
                            (enrolments = enrolmentDskServiceManager.getCourseEnrolments(courseSiteId, "personBatchUid"))
                    );
                    if (enrolments == null) {
                        model.addAttribute("errorCode", "error.search.no_results");
                    }
                }
                selectedEnrolmentIds = getSelectedEnrolmentIds(enrolments, checkBoxKey, request);
            } else if (type.equals(B2Enrolment.ORGANISATION_ENROLMENT_TYPE)) {
                enrolmentType = B2Enrolment.ORGANISATION_ENROLMENT_TYPE;
                checkBoxKey = Parameter.SELECTED_COURSE_ENROLMENTS_LIST.getName();

                dataSourceKeysMap = this.getOrganisationDataSourceKeysMap();
                rolesMap = this.getOrganisationRolesMap();

                String courseSiteId = null;
                if (StringUtil.notEmpty(courseSiteId = form.getSearchText())) {
                    returnString = "success_organisation_enrolment";
                    // sort by personBatchUid
                    model.addAttribute(
                            modelAndSessionAttributesMap.get("b2.model.key.organisation_enrolments"),
                            (enrolments = enrolmentDskServiceManager.getOrganisationEnrolments(courseSiteId, "personBatchUid"))
                    );

                    if (enrolments == null) {
                        model.addAttribute("errorCode", "error.search.no_results");
                    }
                }
                selectedEnrolmentIds = getSelectedEnrolmentIds(enrolments, checkBoxKey, request);
            } else if (type.equals(B2Enrolment.USER_ENROLMENT_TYPE)) {
                enrolmentType = B2Enrolment.USER_ENROLMENT_TYPE;
                checkBoxKey = Parameter.SELECTED_USER_ENROLMENTS_LIST.getName();

                dataSourceKeysMap = this.getUserDataSourceKeysMap();
                rolesMap = this.getCourseOrOrganisationRolesMap();

                String selectedUserName = form.getSearchText();
                if (StringUtil.notEmpty(selectedUserName)) {
                    returnString = "success_user_enrolment";
                    // Sort by courseBatchUid
                    model.addAttribute(
                            modelAndSessionAttributesMap.get("b2.model.key.user_enrolments"),
                            (enrolments = enrolmentDskServiceManager.getUserEnrolments(form, "courseSiteBatchUid"))
                    );
                    if (enrolments == null) {
                        model.addAttribute("errorCode", "error.search.no_results");
                    }
                }
                selectedEnrolmentIds = getSelectedEnrolmentIds(enrolments, checkBoxKey, request);
            }

            boolean isFormReady = isFormReadyToPersist(form);
            boolean isRecordSelected = ArrayUtils.isNotEmpty(selectedEnrolmentIds);
            if (isFormReady && isRecordSelected && StringUtil.notEmpty(returnString)) {
                //To check if the specified course/organisation role is in the customised role list specified in blackboard.xml
                if (isSelectedDataSourceKeyValid(model, form, dataSourceKeysMap) && isSelectedRoleValid(model, form, rolesMap)) {
                    form.setChangeComments(request.getParameter(Parameter.BB_CHANGE_COMMENTS_TEXTAREA_KEY.getName()));
                    String changeComments = form.getChangeComments();
                    List<PersistenceOutcome> outcomes = enrolmentDskServiceManager.persistEnrolmentsByDAO(selectedEnrolmentIds, form, enrolmentType);
                    //List<PersistenceOutcome> outcomes = enrolmentDskServiceManager.persistEnrolments(selectedEnrolmentIds, form, enrolmentType);
                    serviceManager.persistOutcome(DSK_TOOL_TYPE, changeComments, outcomes);
                    model.addAttribute("SELECTED_ENROLMENTS", selectedEnrolmentIds);
                    model.addAttribute("changeComments", changeComments);
                    model.addAttribute("outcomes", outcomes);
                    model.addAttribute("receiptType", "SUCCESS");
                    populate(model, form, request.getRequestURI());
                    status.isComplete();
                    return returnString;
                }
            } else {
                setUpdateError(model, request, isFormReady, isRecordSelected);
            }
            model = getPostModel(model, request, checkBoxKey);
        }
        setEnrolmentComparators(model);
        return getReturn(type, model);
    }


    private String[] getSelectedEnrolmentIds(List<B2Enrolment> enrolments, String checkBoxKey, HttpServletRequest request) {
        String[] selectedEnrolmentIds = null;
        try {
            selectedEnrolmentIds = request.getParameterValues(checkBoxKey);
        } catch (NullPointerException e) {
            logger.error("Method: getSelectedEnrolments : " + e.getMessage());
        }

        //modelAndSessionAttributesMap is defined in blackboard.xml (selectAllMode=fromAllList)
        String selectAllMode = request.getParameter(modelAndSessionAttributesMap.get("bb.inventory.select.all.mode.key"));
        if (ArrayUtils.isEmpty(selectedEnrolmentIds)
                && StringUtil.notEmpty(selectAllMode)
                && selectAllMode.equals(modelAndSessionAttributesMap.get("bb.inventory.select.all.mode.value"))
                && enrolments != null) {
            selectedEnrolmentIds = new String[enrolments.size()];
            int index = 0;
            for (B2Enrolment enrolment : enrolments) {
                selectedEnrolmentIds[index] = enrolment.getEnrollment().getId().toExternalString();
                index++;
            }
        }
        return selectedEnrolmentIds;
    }

    private String getReturn(String type, ModelMap model) {
        if (StringUtil.notEmpty(type)) {
            if (type.equals(B2Enrolment.COURSE_ENROLMENT_TYPE)) {
                return "cenrolment";
            } else if (type.equals(B2Enrolment.ORGANISATION_ENROLMENT_TYPE)) {
                return "oenrolment";
            } else if (type.equals(B2Enrolment.USER_ENROLMENT_TYPE)) {
                return "uenrolment";
            }
        }
        model.addAttribute("receiptType", "FAIL");
        return "fail";
    }
}
