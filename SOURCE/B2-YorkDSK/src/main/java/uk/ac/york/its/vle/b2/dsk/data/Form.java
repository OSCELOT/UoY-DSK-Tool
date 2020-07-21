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

import blackboard.util.StringUtil;

import java.util.Calendar;


/**
 * @author Kelvin Hai {@link <a href="mailto:kelvin.hai@york.ac.uk">kelvin.hai@york.ac.uk</a>}
 * @version $Revision$ $Date$
 */
public class Form {
	private String contextPath;
	private String requestURI;
	private String recallQueryString;
	private Calendar createDateCalendar=Calendar.getInstance();;
	
	private String category;
	private String organisationCategory;
	private String dateSearchOperator;
	private String searchKey;
	private String searchOperator;
	private String searchText;
	
	private boolean isUpdateRequired;
	private boolean isCourseRoleRequired;
	private boolean isStatusUpdateRequired;
	private boolean isAvailabilityUpdateRequired;
	private boolean isDataSourceKeyUpdateRequired;
	private String selectedCourseRole;
	private String selectedRowStatus;
	private String selectedAvailability;
	private String selectedDataSourceKey;
	private String[] selectedUserNames;
	private String selectedUserName;
	private String changeComments;


	/**
	 * @return the contextPath
	 */
	public String getContextPath() {
		return contextPath;
	}
	
	/**
	 * @param contextPath the contextPath to set
	 */
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
	
	/**
	 * @return the requestURI
	 */
	public String getRequestURI() {
		return requestURI;
	}
	
	/**
	 * @param requestURI the requestURI to set
	 */
	public void setRequestURI(String requestURI) {
		this.requestURI = requestURI;
	}
	
	/**
	 * @return the recallQueryString
	 */
	public String getRecallQueryString() {
		return recallQueryString;
	}
	
	/**
	 * @param recallQueryString the recallQueryString to set
	 */
	public void setRecallQueryString(String recallQueryString) {
		this.recallQueryString = recallQueryString;
	}
	
	/**
	 * @return the createDateCalendar
	 */
	public Calendar getCreateDateCalendar() {
		return createDateCalendar;
	}
	
	/**
	 * @param createDateCalendar the createDateCalendar to set
	 */
	public void setCreateDateCalendar(Calendar createDateCalendar) {
		this.createDateCalendar = createDateCalendar;
	}
	
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getOrganisationCategory() {
		return organisationCategory;
	}

	public void setOrganisationCategory(String organisationCategory) {
		this.organisationCategory = organisationCategory;
	}

	/**
	 * @return the dateSearchOperator
	 */
	public String getDateSearchOperator() {
		return dateSearchOperator;
	}
	
	/**
	 * @param dateSearchOperator the dateSearchOperator to set
	 */
	public void setDateSearchOperator(String dateSearchOperator) {
		this.dateSearchOperator = dateSearchOperator;
	}
	
	/**
	 * @return the searchKey
	 */
	public String getSearchKey() {
		return searchKey;
	}
	
	/**
	 * @param searchKey the searchKey to set
	 */
	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}
	
	/**
	 * @return the searchOperator
	 */
	public String getSearchOperator() {
		return searchOperator;
	}
	
	/**
	 * @param searchOperator the searchOperator to set
	 */
	public void setSearchOperator(String searchOperator) {
		this.searchOperator = searchOperator;
	}
	
	/**
	 * @return the searchText
	 */
	public String getSearchText() {
		return searchText;
	}
	
	/**
	 * @param searchText the searchText to set
	 */
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	/**
	 * @return the isUpdateRequired
	 */
	public boolean getIsUpdateRequired() {
		return isUpdateRequired;
	}

	/**
	 * @param isUpdateRequired the isUpdateRequired to set
	 */
	public void setIsUpdateRequired(boolean isUpdateRequired) {
		this.isUpdateRequired = isUpdateRequired;
	}

	/**
	 * @return the isCourseRoleRequired
	 */
	public boolean getIsCourseRoleRequired() {
		return isCourseRoleRequired;
	}

	/**
	 * @param isCourseRoleRequired the isCourseRoleRequired to set
	 */
	public void setIsCourseRoleRequired(boolean isCourseRoleRequired) {
		this.isCourseRoleRequired = isCourseRoleRequired;
	}

	/**
	 * @return the isStatusUpdateRequired
	 */
	public boolean getIsStatusUpdateRequired() {
		return isStatusUpdateRequired;
	}

	/**
	 * @param isStatusUpdateRequired the isStatusUpdateRequired to set
	 */
	public void setIsStatusUpdateRequired(boolean isStatusUpdateRequired) {
		this.isStatusUpdateRequired = isStatusUpdateRequired;
	}

	/**
	 * @return the isAvailabilityUpdateRequired
	 */
	public boolean getIsAvailabilityUpdateRequired() {
		return isAvailabilityUpdateRequired;
	}

	/**
	 * @param isAvailabilityUpdateRequired the isAvailabilityUpdateRequired to set
	 */
	public void setIsAvailabilityUpdateRequired(boolean isAvailabilityUpdateRequired) {
		this.isAvailabilityUpdateRequired = isAvailabilityUpdateRequired;
	}

	/**
	 * @return the isDataSourceKeyUpdateRequired
	 */
	public boolean getIsDataSourceKeyUpdateRequired() {
		return isDataSourceKeyUpdateRequired;
	}

	/**
	 * @param isDataSourceKeyUpdateRequired the isDataSourceKeyUpdateRequired to set
	 */
	public void setIsDataSourceKeyUpdateRequired(boolean isDataSourceKeyUpdateRequired) {
		this.isDataSourceKeyUpdateRequired = isDataSourceKeyUpdateRequired;
	}


	/**
	 * @return the selectedCourseRole
	 */
	public String getSelectedCourseRole() {
		return selectedCourseRole;
	}

	/**
	 * @param selectedCourseRole the selectedCourseRole to set
	 */
	public void setSelectedCourseRole(String selectedCourseRole) {
		this.selectedCourseRole = selectedCourseRole;
	}

	/**
	 * @return the selectedRowStatus
	 */
	public String getSelectedRowStatus() {
		return selectedRowStatus;
	}

	/**
	 * @param selectedRowStatus the selectedRowStatus to set
	 */
	public void setSelectedRowStatus(String selectedRowStatus) {
		this.selectedRowStatus = selectedRowStatus;
	}

	/**
	 * @return the selectedAvailability
	 */
	public String getSelectedAvailability() {
		return selectedAvailability;
	}

	/**
	 * @param selectedAvailability the selectedAvailability to set
	 */
	public void setSelectedAvailability(String selectedAvailability) {
		this.selectedAvailability = selectedAvailability;
	}

	/**
	 * @return the selectedDataSourceKey
	 */
	public String getSelectedDataSourceKey() {
		return selectedDataSourceKey;
	}

	/**
	 * @param selectedDataSourceKey the selectedDataSourceKey to set
	 */
	public void setSelectedDataSourceKey(String selectedDataSourceKey) {
		this.selectedDataSourceKey = selectedDataSourceKey;
	}

	/**
	 * @return the selectedUserNames
	 */
	public String[] getSelectedUserNames() {
		return selectedUserNames;
	}

	/**
	 * @param selectedUserNames the selectedUserNames to set
	 */
	public void setSelectedUserNames(String[] selectedUserNames) {
		this.selectedUserNames = selectedUserNames;
	}

	/**
	 * @return the selectedUserName
	 */
	public String getSelectedUserName() {
		return selectedUserName;
	}

	/**
	 * @param selectedUserName the selectedUserName to set
	 */
	public void setSelectedUserName(String selectedUserName) {
		this.selectedUserName = selectedUserName;
	}


	public String getChangeComments() {
		return changeComments;
	}

	public void setChangeComments(String changeComments) {
	    if(StringUtil.notEmpty(changeComments)){
	        //remove HTML tags
            changeComments=changeComments.replaceAll("\\<.*?\\>", "");
        }
		this.changeComments = changeComments;
	}
	
}
