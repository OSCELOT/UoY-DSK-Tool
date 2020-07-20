/**
 *  Copyright (C) 2016 University of York, UK.
 *
 *  This project was initiated through a donation of source code by the
 *  University of York, UK. It contains free software; you can redistribute
 *  it and/or modify it under the terms of the GNU General Public License as
 *  published by the Free Software Foundation; either version 2 of the
 *  License, or any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *  For more information please contact:
 *
 *  Web Services Group
 *  IT Service
 *  University of York
 *  YO10 5DD
 *  United Kingdom
 */
package uk.ac.york.its.vle.b2.dsk.data;

/**
 * @author Kelvin Hai {@link <a href="mailto:kelvin.hai@york.ac.uk">kelvin.hai@york.ac.uk</a>}
 * @version $Revision$ $Date$
 */

public enum Parameter {
	ROLE,
	COURSE_ROLE,
	DATE_SEARCH_OPERATOR,
	SEARCH_KEY,
	SEARCH_OPERATOR,
	SEARCH_TEXT,
	GO,
	HASH,
	PLUGIN_ID,
	MODULE_ID,
	PORTAL_VIEW_ID,
	TAB_TAB_GROUP_ID,
	SELECTED_COURSE_KEY,
	SELECTED_ORGANISATION_KEY,
	SELECTED_USER_NAME,
	SELECTED_USER_NAME_LIST,
	SELECTED_USER_ENROLMENTS_LIST,
	SELECTED_COURSE_ENROLMENTS_LIST,
	USER_ID,
	SESSION_ID,
	PASSWORD,
	SELECTABLE_ROLES,
	SELECTABLE_ORGANISATION_ROLES,
    CHANGE_COMMENTS_TEXTAREA_KEY,
    BB_CHANGE_COMMENTS_TEXTAREA_KEY
	;
	public String getName(){
		
		switch (this) {
		case SEARCH_KEY: return "searchKey";
		case SEARCH_OPERATOR: return "searchOperator";
		case SEARCH_TEXT: return "searchText";
		case DATE_SEARCH_OPERATOR: return "dateSearchOperator";
		case COURSE_ROLE: return "courseRole";
		case ROLE: return "role";
		case GO: return "go";
		case HASH: return "hash";
		case PLUGIN_ID: return "plugin_id";
		case MODULE_ID: return "module_id";
		case PORTAL_VIEW_ID: return "portal_view_id";
		case TAB_TAB_GROUP_ID: return "tab_tab_group_id";
		case SELECTED_COURSE_KEY: return "selectedCourseId";
		case SELECTED_ORGANISATION_KEY: return "selectedOrganisationId";
		case SELECTED_USER_NAME: return "selectedUserName";
		case SELECTED_USER_NAME_LIST: return "selectedUserNames";
		case SELECTED_USER_ENROLMENTS_LIST: return "selectedUserEnrolments";
		case SELECTED_COURSE_ENROLMENTS_LIST: return "selectedCourseEnrolments";
		case USER_ID: return "user_id";
		case SESSION_ID: return "session_id";
		case PASSWORD: return "password";
		case SELECTABLE_ROLES: return "selectable_roles";
		case SELECTABLE_ORGANISATION_ROLES:  return "selectable_organisation_roles";
		// CHANGE_COMMENTS_TEXTAREA_KEY is used as the name for bbNG:textbox
		case CHANGE_COMMENTS_TEXTAREA_KEY: return "changeComments";
		// BB_CHANGE_COMMENTS_TEXTAREA_KEY = CHANGE_COMMENTS_TEXTAREA_KEY+"text";
        // BB_CHANGE_COMMENTS_TEXTAREA_KEY is used for request.getParameter(BB_CHANGE_COMMENTS_TEXTAREA_KEY);
		case BB_CHANGE_COMMENTS_TEXTAREA_KEY: return CHANGE_COMMENTS_TEXTAREA_KEY.getName()+"text";
		default:return name();
		}
	}
}

//		case SELECTED_COURSE_KEY: return "selectedCourseBatchUid";