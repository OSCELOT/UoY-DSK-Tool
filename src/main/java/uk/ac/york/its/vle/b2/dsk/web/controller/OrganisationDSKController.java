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

import blackboard.util.StringUtil;
import uk.ac.york.its.vle.b2.dsk.data.B2Course;
import uk.ac.york.its.vle.b2.dsk.data.Form;
import uk.ac.york.its.vle.b2.dsk.data.Parameter;
import uk.ac.york.its.vle.b2.dsk.model.PersistenceOutcome;
import uk.ac.york.its.vle.b2.dsk.service.CourseDSKServiceManager;


@Controller
@RequestMapping(value = "/organisation")
@SessionAttributes(types = {Form.class})
public class OrganisationDSKController extends DSKController {
    private static final Logger logger = LoggerFactory.getLogger(OrganisationDSKController.class);
    private static final String DSK_TOOL_TYPE = "organisation_dsk";

    @Autowired
    private CourseDSKServiceManager courseDskServiceManager;


    @RequestMapping(value = "{type}", method = RequestMethod.GET)
    public String showForm(@PathVariable String type, HttpServletRequest request, ModelMap model) {
        clearSessionAttributes(request);
        setForm(model);
        return getReturn(type, model);

    }

    // Method to accept request form inventory list
    @RequestMapping(value = "{type}/list", method = RequestMethod.GET)
    public String showList(@PathVariable String type, HttpServletRequest request, ModelMap model) {
        setOrganisationComparators(model);
        model = getGetModel(model, request, Parameter.SELECTED_ORGANISATION_KEY.getName());
        return getReturn(type, model);
    }

    @RequestMapping(value = "{type}", method = RequestMethod.POST)
    public String processForm(
            @PathVariable String type,
            HttpServletRequest request,
            @ModelAttribute Form form,
            BindingResult result,
            SessionStatus status,
            ModelMap model) {

        if (StringUtil.notEmpty(type)) {
            if (form != null) {
                form.setCreateDateCalendar(getCalendar(request));
            }

            List<B2Course> courses = null;
            String serachText = form.getSearchText();
            if (type.equals("organisation") && StringUtil.notEmpty(serachText)) {
                //Sort by courseId propertyName
                model.addAttribute(
                        modelAndSessionAttributesMap.get("b2.model.key.organisations"),
                        (courses = courseDskServiceManager.getOrganisations(form, "courseId"))
                );
                if (courses == null) {
                    model.addAttribute("errorCode", "error.search.no_results");
                }
            } else if (type.equals("category")) {
                //Sort by courseId propertyName
                model.addAttribute(
                        modelAndSessionAttributesMap.get("b2.model.key.organisations"),
                        (courses = courseDskServiceManager.getCoursesByOrganisationCategory(form, "courseId"))
                );
                if (courses == null) {
                    model.addAttribute("errorCode", "error.search.no_results");
                }
            }

            String[] selectedCourseIds = selectedCourseIds(courses, Parameter.SELECTED_ORGANISATION_KEY.getName(), request);
            boolean isFormReady = isFormReadyToPersist(form);
            boolean isRecordSelected = ArrayUtils.isNotEmpty(selectedCourseIds);

            if (isFormReady && isRecordSelected) {
                //To check if the specified data source key is in the customised data source key list specified in blackboard.xml
                if (isSelectedDataSourceKeyValid(model, form, this.getOrganisationDataSourceKeysMap())) {
                    form.setChangeComments(request.getParameter(Parameter.BB_CHANGE_COMMENTS_TEXTAREA_KEY.getName()));
                    String changeComments = form.getChangeComments();
                    List<PersistenceOutcome> outcomes = courseDskServiceManager.persistCourse(selectedCourseIds, form);
                    serviceManager.persistOutcome(DSK_TOOL_TYPE, changeComments, outcomes);
                    model.addAttribute("SELECTED_COURSES", selectedCourseIds);
                    model.addAttribute("changeComments", changeComments);
                    model.addAttribute("outcomes", outcomes);
                    model.addAttribute("receiptType", "SUCCESS");
                    populate(model, form, request.getRequestURI());
                    status.isComplete();
                    clearSessionAttributes(request);
                    return "success_organisation";
                }
            } else {
                setUpdateError(model, request, isFormReady, isRecordSelected);
            }
            setOrganisationComparators(model);
            model = getPostModel(model, request, Parameter.SELECTED_ORGANISATION_KEY.getName());
        }

        return getReturn(type, model);
    }


    private String getReturn(String type, ModelMap model) {
        if (StringUtil.notEmpty(type)) {
            model.addAttribute("inventory_type", "organisation");
            if (type.equals("category")) {
                model.addAttribute("inventory_list_type", "category");
                return "organisation_category";
            } else if (type.equals("organisation")) {
                model.addAttribute("inventory_list_type", "organisation");
                return "organisation_organisation";
            }
        }
        model.addAttribute("receiptType", "FAIL");
        return "fail";
    }
}
