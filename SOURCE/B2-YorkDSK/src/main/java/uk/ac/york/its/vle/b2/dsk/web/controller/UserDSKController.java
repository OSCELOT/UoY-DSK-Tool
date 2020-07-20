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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import blackboard.util.StringUtil;

import uk.ac.york.its.vle.b2.dsk.data.B2User;
import uk.ac.york.its.vle.b2.dsk.data.Form;
import uk.ac.york.its.vle.b2.dsk.data.Parameter;
import uk.ac.york.its.vle.b2.dsk.model.PersistenceOutcome;
import uk.ac.york.its.vle.b2.dsk.service.UserDSKServiceManager;

@Controller
@SessionAttributes(types = {Form.class})
public class UserDSKController extends DSKController {
    private static final Logger logger = LoggerFactory.getLogger(UserDSKController.class);
    private static final String DSK_TOOL_TYPE = "user_dsk";

    @Autowired
    private UserDSKServiceManager userDskServiceManager;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String user(HttpServletRequest request, ModelMap model) {
        clearSessionAttributes(request);
        setForm(model);
        return "user";
    }

    @RequestMapping(value = "/user_list", method = RequestMethod.GET)
    public String courseList(ModelMap model, HttpServletRequest request) {
        setUserComparators(model);
        getGetModel(model, request, Parameter.SELECTED_USER_NAME_LIST.getName());
        return "user";
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public String processUserSubmit(
            HttpServletRequest request,
            @ModelAttribute Form form,
            BindingResult result,
            SessionStatus status,
            ModelMap model) {


        List<B2User> users = null;
        // sort by userName
        if (null != (users = userDskServiceManager.getUsersByUserSearch(form, "userName"))) {
            setUserComparators(model);
            model.addAttribute(modelAndSessionAttributesMap.get("b2.model.key.users"), users);
        } else {
            // To show error.
            model.addAttribute("errorCode", "error.search.no_results");
        }

        String[] selectedUserNames = getSelectedUserNames(users, Parameter.SELECTED_USER_NAME_LIST.getName(), request);
        boolean isFormReady = isFormReadyToPersist(form);
        boolean isRecordSelected = ArrayUtils.isNotEmpty(selectedUserNames);

        if (isFormReady && isRecordSelected) {
            //To check if the specified data source key is in the customised data source key list specified in blackboard.xml
            if (isSelectedDataSourceKeyValid(model, form, this.getUserDataSourceKeysMap())) {
                form.setChangeComments(request.getParameter(Parameter.BB_CHANGE_COMMENTS_TEXTAREA_KEY.getName()));
                form.setSelectedUserNames(selectedUserNames);
                String changeComments = form.getChangeComments();
                List<PersistenceOutcome> outcomes = userDskServiceManager.persistPerson(form);
                serviceManager.persistOutcome(DSK_TOOL_TYPE, changeComments, outcomes);
                model.addAttribute("usernames", selectedUserNames);
                model.addAttribute("changeComments", changeComments);
                model.addAttribute("outcomes", outcomes);
                model.addAttribute("receiptType", "SUCCESS");
                populate(model, form, request.getRequestURI());
                status.isComplete();
                clearSessionAttributes(request);
                return "success_user";
            }
        } else {
            setUpdateError(model, request, isFormReady, isRecordSelected);
        }
        getPostModel(model, request, Parameter.SELECTED_USER_NAME_LIST.getName());
        return "user";
    }


    private String[] getSelectedUserNames(List<B2User> users, String checkBoxKey, HttpServletRequest request) {
        String[] selectedUserNames = null;
        try {
            selectedUserNames = request.getParameterValues(checkBoxKey);
        } catch (NullPointerException e) {
            logger.error("Method: processUserSubmit: " + e.getMessage());
        }

        //modelAndSessionAttributesMap is defined in blackboard.xml (selectAllMode=fromAllList)
        String selectAllMode = request.getParameter(modelAndSessionAttributesMap.get("bb.inventory.select.all.mode.key"));
        if (ArrayUtils.isEmpty(selectedUserNames)
                && StringUtil.notEmpty(selectAllMode)
                && selectAllMode.equals(modelAndSessionAttributesMap.get("bb.inventory.select.all.mode.value"))
                && users != null) {
            selectedUserNames = new String[users.size()];
            int index = 0;
            for (B2User user : users) {
                selectedUserNames[index] = user.getUserName();
                index++;
            }
        }

        return selectedUserNames;
    }


}
