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
package uk.ac.york.its.vle.b2.dsk.service;

/**
 * @author Kelvin Hai {@link <a href="mailto:kelvin.hai@york.ac.uk">kelvin.hai@york.ac.uk</a>}
 * @version $Revision$ $Date$
 */

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import uk.ac.york.its.vle.b2.dsk.data.GenericFormSelectOption;
import uk.ac.york.its.vle.b2.dsk.data.Parameter;
import blackboard.admin.data.datasource.DataSource;
import blackboard.data.course.CourseMembership;
import blackboard.persist.SearchOperator;
import blackboard.persist.course.CourseSearch;
import blackboard.persist.user.UserSearch;

public interface ReferenceServiceManager {
	
	
	public List<GenericFormSelectOption> getCourseCategories();
	public List<GenericFormSelectOption> getOrganisationCategories();
	
	public Map<Parameter, String> getParameterEntities();
	
	public List<GenericFormSelectOption> getCourseRoles();
	public List<GenericFormSelectOption> getFilteredCourseRoles(HttpServletRequest request);
	public List<CourseMembership.Role> getCourseRolesList();
	public Map<CourseMembership.Role, String> getCourseRolesMap();
	
	public List<GenericFormSelectOption> getOrganisationRoles();
	public List<GenericFormSelectOption> getFilteredOrganisationRoles(HttpServletRequest request);
	public List<CourseMembership.Role> getOrganisationRolesList();
	public Map<CourseMembership.Role, String> getOrganisationRolesMap();
	
	public List<GenericFormSelectOption> getCourseOrOrganisationRoles();
	public Map<CourseMembership.Role, String> getCourseOrOrganisationRolesMap();
	
	public List<GenericFormSelectOption> getOrganisationSearchKeys();
	public List<CourseSearch.SearchKey> getOrganisationSearchKeysList();
	public Map<CourseSearch.SearchKey, String> getOrganisationSearchKeysMap();
	
	public List<GenericFormSelectOption> getCourseSearchKeys();
	public List<CourseSearch.SearchKey> getCourseSearchKeysList();
	public Map<CourseSearch.SearchKey, String> getCourseSearchKeysMap();
	
	public List<GenericFormSelectOption> getUserSearchKeys();
	public List<UserSearch.SearchKey> getUserSearchKeysList();
	public Map<UserSearch.SearchKey, String> getUserSearchKeysMap();
	
	public List<GenericFormSelectOption> getCourseSearchOperators();
	public List<SearchOperator> getCourseSearchOperatorsList();
	public Map<SearchOperator, String> getCourseSearchOperatorsMap();
	
	public List<GenericFormSelectOption> getUserSearchOperators();
	public List<SearchOperator> getUserSearchOperatorsList();
	public Map<SearchOperator, String> getUserSearchOperatorsMap();
	
	public List<GenericFormSelectOption> getDateSearchOperators();
	public List<SearchOperator> getDateSearchOperatorsList();
	public Map<SearchOperator, String> getDateSearchOperatorsMap();
	
	public List<DataSource> getDataSourceKeys();
	public Map<String, String> getDataSourceKeysMap();
	
	public List<DataSource> getUserDataSourceKeys();
	public Map<String, String> getUserDataSourceKeysMap();
	
	public List<DataSource> getCourseDataSourceKeys();
	public Map<String, String> getCourseDataSourceKeysMap();
	
	public List<DataSource> getOrganisationDataSourceKeys();
	public Map<String, String> getOrganisationDataSourceKeysMap();
	
	public List<DataSource> getEnrolmentDataSourceKeys();
	public Map<String, String> getEnrolmentDataSourceKeysMap();
	
	public List<DataSource> getUserEnrolmentDataSourceKeys();
	public Map<String, String> getUserEnrolmentDataSourceKeysMap();
	
	public List<DataSource> getCourseEnrolmentDataSourceKeys();
	public Map<String, String> getCourseEnrolmentDataSourceKeysMap();
}
