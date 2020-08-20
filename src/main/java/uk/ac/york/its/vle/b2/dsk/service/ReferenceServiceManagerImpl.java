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

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.stereotype.Service;

import uk.ac.york.its.vle.b2.dsk.data.GenericFormSelectOption;
import uk.ac.york.its.vle.b2.dsk.data.GenericFormSelectOptionImpl;
import uk.ac.york.its.vle.b2.dsk.data.Parameter;


import blackboard.admin.data.category.CourseCategory;
import blackboard.admin.data.category.OrganizationCategory;
import blackboard.admin.data.datasource.DataSource;
import blackboard.admin.persist.category.CourseCategoryLoader;
import blackboard.admin.persist.category.OrganizationCategoryLoader;
import blackboard.admin.persist.datasource.DataSourceLoader;
import blackboard.data.course.CourseMembership;
import blackboard.persist.PersistenceException;
import blackboard.persist.SearchOperator;
import blackboard.persist.course.CourseSearch;
import blackboard.persist.user.UserSearch;
import blackboard.util.StringUtil;

/**
 * @author Kelvin Hai {@link <a href="mailto:kelvin.hai@york.ac.uk">kelvin.hai@york.ac.uk</a>}
 * @version $Revision$ $Date$
 */

@Service
public class ReferenceServiceManagerImpl implements ReferenceServiceManager {
	
	private static final Logger logger = LoggerFactory.getLogger(ReferenceServiceManagerImpl.class);
	
	@Autowired
	private DataSourceLoader dataSourceLoader;
	
	@Autowired
	private CourseCategoryLoader categoryLoader;
	
	@Autowired
	private OrganizationCategoryLoader organisationCategoryLoader;
	
	@Resource
	private Map<String, String> modelAndSessionAttributesMap;
	
	@Resource
	private Map<CourseMembership.Role, String> courseRolesMap;
	
	@Resource
	private Map<CourseMembership.Role, String> organisationRolesMap;
	
	@Resource
	private Map<CourseMembership.Role, String> courseOrOrganisationRolesMap;
	
	@Resource
	private Map<CourseSearch.SearchKey, String> courseSearchKeysMap;
	
	@Resource
	private Map<CourseSearch.SearchKey, String> organisationSearchKeysMap;
	
	@Resource
	private Map<UserSearch.SearchKey, String> userSearchKeysMap;

	@Resource
	private Map<SearchOperator, String> courseSearchOperatorsMap;
	
	@Resource
	private Map<SearchOperator, String> userSearchOperatorsMap;
	
	@Resource
	private Map<SearchOperator, String> dateSearchOperatorsMap;
	
	@Resource
	private List<String> categoryExcludedPatterns;

	@Resource
	private List<String> organisationCategoryExcludedPatterns;

	@Resource
	private List<String> dataSourceKeyExcludedPatterns;
	
	@Resource
	private List<String> userDataSourceKeyExcludedPatterns;

	@Resource
	private List<String> courseDataSourceKeyExcludedPatterns;

	@Resource
	private List<String> organisationDataSourceKeyExcludedPatterns;

	@Resource
	private List<String> enrolmentDataSourceKeyExcludedPatterns;
	
	@Resource
	private List<String> userEnrolmentDataSourceKeyExcludedPatterns;
	
	@Resource
	private List<String> courseEnrolmentDataSourceKeyExcludedPatterns;
	
	
	public List<GenericFormSelectOption> getCourseCategories(){
		List<CourseCategory> categories=getCourseCategoriesList();
		if(null!=categories && categories.size()>0){
			List<GenericFormSelectOption> list=new LinkedList<GenericFormSelectOption>();
			for(CourseCategory category:categories){
				list.add(new GenericFormSelectOptionImpl(category));
			}
			if(list.size()>0){
				return list;
			}
		}
		return null;
	}

	public List<GenericFormSelectOption> getOrganisationCategories(){
		List<OrganizationCategory> categories=getOrganisationCategoriesList();
		if(null!=categories && categories.size()>0){
			List<GenericFormSelectOption> list=new LinkedList<GenericFormSelectOption>();
			for(OrganizationCategory category:categories){
				list.add(new GenericFormSelectOptionImpl(category));
			}
			if(list.size()>0){
				return list;
			}
		}
		return null;
	}
	
	public List<CourseCategory> getCourseCategoriesList(){
		List<CourseCategory> categories=null;
		try {
			categories=categoryLoader.load( new CourseCategory());
			if(null!=categories && categories.size()>0){
				PropertyComparator.sort (categories, new MutableSortDefinition ( "batchUid", true, true));
				if(null!=categoryExcludedPatterns){
					Collections.sort(categoryExcludedPatterns);
					String batchUid=null;
					for(String regex:categoryExcludedPatterns){
						if(StringUtil.notEmpty(regex)){
							List<CourseCategory> categories2Removed=new LinkedList<CourseCategory>();
							for(CourseCategory category:categories){
								if(StringUtil.notEmpty(batchUid=category.getBatchUid())
										&& batchUid.matches(regex)){
									categories2Removed.add(category);
								}
							}
							
							if(null!=categories2Removed && categories2Removed.size()>0){
								categories.removeAll(categories2Removed);
							}
						}
					}
				}
			}

			if(null!=categories){
				return Collections.unmodifiableList (categories);
			}
		} catch (PersistenceException e) {
			logger.error("Method: getCourseCategoriesList: "+e.getMessage());
		}
		return null;
	}
	
	public List<OrganizationCategory> getOrganisationCategoriesList(){
		List<OrganizationCategory> categories=null;
		try {
			categories=organisationCategoryLoader.load( new OrganizationCategory());
			
			if(null!=categories && categories.size()>0){
				PropertyComparator.sort (categories, new MutableSortDefinition ( "batchUid", true, true));
				if(null!=organisationCategoryExcludedPatterns){
					Collections.sort(organisationCategoryExcludedPatterns);
					String batchUid=null;
					for(String regex:organisationCategoryExcludedPatterns){
						if(StringUtil.notEmpty(regex)){
							List<OrganizationCategory> categories2Removed=new LinkedList<OrganizationCategory>();
							for(OrganizationCategory category:categories){
								if(StringUtil.notEmpty(batchUid=category.getBatchUid())
										&& batchUid.matches(regex)){
									categories2Removed.add(category);
								}
							}
							
							if(null!=categories2Removed && categories2Removed.size()>0){
								categories.removeAll(categories2Removed);
							}
						}
					}
				}
			}

			if(null!=categories){
				return Collections.unmodifiableList (categories);
			}
		} catch (PersistenceException e) {
			logger.error("Method: getOrganisationCategoriesList: "+e.getMessage());
		}
		return null;
	}

	public List<CourseSearch.SearchKey> getCourseSearchKeysList() {
		return getCustomisedCourseSearchKeys(courseSearchKeysMap);
	}

	public List<CourseSearch.SearchKey> getOrganisationSearchKeysList() {
		return getCustomisedCourseSearchKeys(organisationSearchKeysMap);
	}

	public List<UserSearch.SearchKey> getUserSearchKeysList() {
		this.setUserSearchKeyEmptyLabelToDefault(userSearchKeysMap);
		if(null!=userSearchKeysMap){
			List<UserSearch.SearchKey> searchKeys=new LinkedList<UserSearch.SearchKey>();
			for(UserSearch.SearchKey sk : userSearchKeysMap.keySet()){
				searchKeys.add(sk);
			}
			
			if(null!=searchKeys && searchKeys.size()>0){
				return searchKeys;
			}
		}
		return null;
	}


	public List<SearchOperator> getCourseSearchOperatorsList() {
		return getCustomisedSearchOperators(courseSearchOperatorsMap);
	}


	public List<SearchOperator> getUserSearchOperatorsList() {
		return getCustomisedSearchOperators(userSearchOperatorsMap);
	}


	public List<SearchOperator> getDateSearchOperatorsList() {
		return getCustomisedSearchOperators(dateSearchOperatorsMap);
	}



	
	public List<GenericFormSelectOption> getFilteredCourseRoles(HttpServletRequest request) {
		List<GenericFormSelectOption> roles = getCourseRoles();
		if(null!=request && null!=roles && roles.size()>0){
			
			String customDataSelectableRole=null;
			try{
				customDataSelectableRole=request.getParameter(Parameter.SELECTABLE_ROLES.getName());
			}catch(NullPointerException e){}

			String[] selectableRoles=null;
			if(StringUtil.notEmpty(customDataSelectableRole) 
					&& null!=(selectableRoles=customDataSelectableRole.trim().split(","))
					&& selectableRoles.length>1){
				for ( Iterator<GenericFormSelectOption> it = roles.iterator(); it.hasNext(); ) {
					GenericFormSelectOption role = (GenericFormSelectOption)it.next();
					if(null!=role){
						boolean isMatched=false;
						for(String roleString:selectableRoles){
							if(StringUtil.notEmpty(roleString) 
									&& StringUtil.notEmpty(role.getKey()) 
									&& roleString.trim().equals(role.getKey())){
								isMatched=true;
								break;
							}
						}
						if(!isMatched){
							it.remove();
						}
					}
				}
				return Collections.unmodifiableList(roles);
			}
		}
		
		return roles;
	}

	public List<GenericFormSelectOption> getFilteredOrganisationRoles(HttpServletRequest request) {
		List<GenericFormSelectOption> roles = getOrganisationRoles();
		if(null!=request && null!=roles && roles.size()>0){
			
			String customDataSelectableRole=null;
			try{
				customDataSelectableRole=request.getParameter(Parameter.SELECTABLE_ROLES.getName());
			}catch(NullPointerException e){}

			String[] selectableRoles=null;
			if(StringUtil.notEmpty(customDataSelectableRole) 
					&& null!=(selectableRoles=customDataSelectableRole.trim().split(","))
					&& selectableRoles.length>1){
				for ( Iterator<GenericFormSelectOption> it = roles.iterator(); it.hasNext(); ) {
					GenericFormSelectOption role = (GenericFormSelectOption)it.next();
					if(null!=role){
						boolean isMatched=false;
						for(String roleString:selectableRoles){
							if(StringUtil.notEmpty(roleString) 
									&& StringUtil.notEmpty(role.getKey()) 
									&& roleString.trim().equals(role.getKey())){
								isMatched=true;
								break;
							}
						}
						if(!isMatched){
							it.remove();
						}
					}
				}
				return Collections.unmodifiableList(roles);
			}
		}
		
		return roles;
	}

	public List<GenericFormSelectOption> getCourseSearchKeys() {
		return getGenericCourseSearchKeyOptionList(courseSearchKeysMap);
	}

	public List<GenericFormSelectOption> getOrganisationSearchKeys() {
		return getGenericCourseSearchKeyOptionList(organisationSearchKeysMap);
	}
	
	public List<GenericFormSelectOption> getUserSearchKeys() {
		this.setUserSearchKeyEmptyLabelToDefault(userSearchKeysMap);
		if(null!=userSearchKeysMap){
			List<GenericFormSelectOption> list=new LinkedList<GenericFormSelectOption>();
			for(Object so : userSearchKeysMap.keySet()){
				GenericFormSelectOptionImpl referenceMap=new GenericFormSelectOptionImpl(so);
				referenceMap.setValue(userSearchKeysMap.get(so));
				list.add(referenceMap);
			}
			
			if(null!=list && list.size()>0){
				return list;
			}
		}
		return null;
	}



	public List<GenericFormSelectOption> getCourseSearchOperators() {
		return getGenericSearchOperatorOptionList(courseSearchOperatorsMap);
	}


	public List<GenericFormSelectOption> getUserSearchOperators() {
		return getGenericSearchOperatorOptionList(userSearchOperatorsMap);		
	}


	public List<GenericFormSelectOption> getDateSearchOperators() {
		return getGenericSearchOperatorOptionList(dateSearchOperatorsMap);
	}
	
	public Map<Parameter, String> getParameterEntities(){
		Map<Parameter, String> params= new HashMap<Parameter, String>();
		params.put(Parameter.ROLE,Parameter.ROLE.getName());
		params.put(Parameter.COURSE_ROLE, Parameter.COURSE_ROLE.getName());
		params.put(Parameter.SEARCH_KEY,Parameter.SEARCH_KEY.getName());
		params.put(Parameter.SEARCH_OPERATOR,Parameter.SEARCH_KEY.getName());
		params.put(Parameter.SEARCH_TEXT,Parameter.SEARCH_KEY.getName());
		params.put(Parameter.DATE_SEARCH_OPERATOR, Parameter.SEARCH_KEY.getName());
		return params;
	}
	
	public Map<CourseMembership.Role, String> getCourseRolesMap() {
		this.setCourseRoleEmptyLabelToDefault(courseRolesMap);
		return courseRolesMap;
	}

	public Map<CourseMembership.Role, String> getOrganisationRolesMap() {
		this.setCourseRoleEmptyLabelToDefault(organisationRolesMap);
		return organisationRolesMap;
	}

	public Map<CourseMembership.Role, String> getCourseOrOrganisationRolesMap() {
		this.setCourseRoleEmptyLabelToDefault(courseOrOrganisationRolesMap);
		return courseOrOrganisationRolesMap;
	}
	
	public Map<CourseSearch.SearchKey, String> getCourseSearchKeysMap() {
		this.setCourseSearchKeyEmptyLabelToDefault(courseSearchKeysMap);
		return courseSearchKeysMap;
	}

	public Map<CourseSearch.SearchKey, String> getOrganisationSearchKeysMap() {
		this.setCourseSearchKeyEmptyLabelToDefault(organisationSearchKeysMap);
		return organisationSearchKeysMap;
	}

	public Map<UserSearch.SearchKey, String> getUserSearchKeysMap() {
		this.setUserSearchKeyEmptyLabelToDefault(userSearchKeysMap);
		return userSearchKeysMap;
	}
	
	public Map<SearchOperator, String> getCourseSearchOperatorsMap() {
		this.setSearchOperatorEmptyLabelToDefault(courseSearchOperatorsMap);
		return courseSearchOperatorsMap;
	}


	public Map<SearchOperator, String> getUserSearchOperatorsMap() {
		this.setSearchOperatorEmptyLabelToDefault(userSearchOperatorsMap);
		return userSearchOperatorsMap;
	}


	public Map<SearchOperator, String> getDateSearchOperatorsMap() {
		this.setSearchOperatorEmptyLabelToDefault(dateSearchOperatorsMap);
		return dateSearchOperatorsMap;
	}



	public List<GenericFormSelectOption> getCourseRoles() {
		return getGenericRoleSelectOptionList(courseRolesMap);
	}
	
	public List<GenericFormSelectOption> getOrganisationRoles() {
		return getGenericRoleSelectOptionList(organisationRolesMap);
	}

	public List<CourseMembership.Role> getCourseRolesList() {
		return getCustomisedRoles(courseRolesMap);
	}
	
	public List<CourseMembership.Role> getOrganisationRolesList() {
		return getCustomisedRoles(organisationRolesMap);
	}

	public List<GenericFormSelectOption> getCourseOrOrganisationRoles() {
		return getGenericRoleSelectOptionList(courseOrOrganisationRolesMap);
	}

	public List<CourseMembership.Role> getCourseOrOrganisationRolesList() {
		return getCustomisedRoles(courseOrOrganisationRolesMap);
	}

	public List<DataSource> getDataSourceKeys(){
		return getFilteredDataSourceKeys(dataSourceKeyExcludedPatterns,"batchUid");
	}


	public Map<String, String> getDataSourceKeysMap(){
		return getFilteredDataSourceKeysMap(getDataSourceKeys());
	}

	public List<DataSource> getUserDataSourceKeys(){
		return getFilteredDataSourceKeys(userDataSourceKeyExcludedPatterns,"batchUid");
	}

	public Map<String, String> getUserDataSourceKeysMap(){
		return getFilteredDataSourceKeysMap(getUserDataSourceKeys());
	}
	
	public List<DataSource> getCourseDataSourceKeys(){
		return getFilteredDataSourceKeys(courseDataSourceKeyExcludedPatterns,"batchUid");
	}

	public Map<String, String> getCourseDataSourceKeysMap(){
		return getFilteredDataSourceKeysMap(getCourseDataSourceKeys());
	}
	
	public List<DataSource> getOrganisationDataSourceKeys(){
		return getFilteredDataSourceKeys(organisationDataSourceKeyExcludedPatterns,"batchUid");
	}
	
	public Map<String, String> getOrganisationDataSourceKeysMap(){
		return getFilteredDataSourceKeysMap(getOrganisationDataSourceKeys());
	}
	
	public List<DataSource> getEnrolmentDataSourceKeys(){
		return getFilteredDataSourceKeys(enrolmentDataSourceKeyExcludedPatterns,"batchUid");
	}

	public Map<String, String> getEnrolmentDataSourceKeysMap(){
		return getFilteredDataSourceKeysMap(getEnrolmentDataSourceKeys());
	}	
	
	public List<DataSource> getUserEnrolmentDataSourceKeys(){
		return getFilteredDataSourceKeys(userEnrolmentDataSourceKeyExcludedPatterns,"batchUid");
	}

	public Map<String, String> getUserEnrolmentDataSourceKeysMap(){
		return getFilteredDataSourceKeysMap(getUserEnrolmentDataSourceKeys());
	}	

	public List<DataSource> getCourseEnrolmentDataSourceKeys(){
		return getFilteredDataSourceKeys(courseEnrolmentDataSourceKeyExcludedPatterns,"batchUid");
	}

	public Map<String, String> getCourseEnrolmentDataSourceKeysMap(){
		return getFilteredDataSourceKeysMap(getCourseEnrolmentDataSourceKeys());
	}	

	private void setUserSearchKeyEmptyLabelToDefault(Map<UserSearch.SearchKey, String> userSearchKeys){
		for(UserSearch.SearchKey sk : userSearchKeys.keySet()){
			String value=userSearchKeys.get(sk);
			if(StringUtil.isEmpty(value)){
				userSearchKeys.put(sk, sk.getName());
			}
		}
	}
	
	private void setCourseSearchKeyEmptyLabelToDefault(Map<CourseSearch.SearchKey, String> courseSearchKeys){
		for(CourseSearch.SearchKey sk : courseSearchKeys.keySet()){
			String value=courseSearchKeys.get(sk);
			if(StringUtil.isEmpty(value)){
				courseSearchKeys.put(sk, sk.name());
			}
		}
	}
	
	private void setSearchOperatorEmptyLabelToDefault(Map<SearchOperator, String> searchOperators){
		for(SearchOperator so : searchOperators.keySet()){
			String value=searchOperators.get(so);
			if(StringUtil.isEmpty(value)){
				searchOperators.put(so, so.getLabel());
			}
		}
	}
	
	
	private void setCourseRoleEmptyLabelToDefault(Map<CourseMembership.Role, String> courseRoles){
		for(CourseMembership.Role role : courseRoles.keySet()){
			String value=courseRoles.get(role);
			if(StringUtil.isEmpty(value)){
				courseRoles.put(role, role.toFieldName());
			}
		}
	}

	private List<GenericFormSelectOption> getGenericCourseSearchKeyOptionList(Map<CourseSearch.SearchKey, String> map) {
		this.setCourseSearchKeyEmptyLabelToDefault(map);
		if(null!=map){
			List<GenericFormSelectOption> list=new LinkedList<GenericFormSelectOption>();
			for(Object so : map.keySet()){
				GenericFormSelectOptionImpl referenceMap=new GenericFormSelectOptionImpl(so);
				referenceMap.setValue(map.get(so));
				list.add(referenceMap);
			}
			
			if(null!=list && list.size()>0){
				return list;
			}
		}
		return null;
	}
	
	private List<GenericFormSelectOption> getGenericSearchOperatorOptionList(Map<SearchOperator, String> map) {
		this.setSearchOperatorEmptyLabelToDefault(map);
		if(null!=map){
			List<GenericFormSelectOption> list=new LinkedList<GenericFormSelectOption>();
			for(Object so : map.keySet()){
				GenericFormSelectOptionImpl referenceMap=new GenericFormSelectOptionImpl(so);
				referenceMap.setValue(map.get(so));
				list.add(referenceMap);
			}
			
			if(null!=list && list.size()>0){
				return list;
			}
		}
		return null;
	}

	
	private List<CourseSearch.SearchKey> getCustomisedCourseSearchKeys(Map<CourseSearch.SearchKey, String> map){
		this.setCourseSearchKeyEmptyLabelToDefault(map);
		if(null!=map){
			List<CourseSearch.SearchKey> searchKeys=new LinkedList<CourseSearch.SearchKey>();
			for(CourseSearch.SearchKey sk : map.keySet()){
				searchKeys.add(sk);
			}
			
			if(null!=searchKeys && searchKeys.size()>0){
				return searchKeys;
			}
		}
		return null;
	}
	
	private List<SearchOperator> getCustomisedSearchOperators(Map<SearchOperator, String> map){
		this.setSearchOperatorEmptyLabelToDefault(map);
		if(null!=map){
			List<SearchOperator> searchOperators=new LinkedList<SearchOperator>();
			for(SearchOperator so : map.keySet()){
				searchOperators.add(so);
			}
			
			if(null!=searchOperators && searchOperators.size()>0){
				return searchOperators;
			}
		}
		return null;
	}

	private List<CourseMembership.Role> getCustomisedRoles(Map<CourseMembership.Role, String> map){
		this.setCourseRoleEmptyLabelToDefault(map);
		if(null!=map){
			List<CourseMembership.Role> roles=new LinkedList<CourseMembership.Role>();
			for(CourseMembership.Role role : map.keySet()){
				roles.add(role);
			}
			
			if(null!=roles && roles.size()>0){
				return roles;
			}
		}
		return null;
	}

	private List<GenericFormSelectOption> getGenericRoleSelectOptionList(Map<CourseMembership.Role, String> map) {
		this.setCourseRoleEmptyLabelToDefault(map);
		if(null!=map){
			List<GenericFormSelectOption> list=new LinkedList<GenericFormSelectOption>();
			for(Object so : map.keySet()){
				GenericFormSelectOptionImpl referenceMap=new GenericFormSelectOptionImpl(so);
				referenceMap.setValue(map.get(so));
				list.add(referenceMap);
			}
			
			if(null!=list && list.size()>0){
				return list;
			}
		}
		return null;
	}


	private Map<String, String> getFilteredDataSourceKeysMap(List<DataSource> dsks){
		if(null!=dsks){
			Map<String, String> map = new HashMap<String, String>();
			String key=null, value=null;
			for(DataSource dsk:dsks){
				if(StringUtil.notEmpty(key=dsk.getId().getExternalString())
						&& StringUtil.notEmpty(value=dsk.getBatchUid())){
					map.put(key, value);
				}
			}
			return map;
		}
		
		return null;
		
	}
	private List<DataSource> getFilteredDataSourceKeys(List<String> excludedPatterns, String sortByString){
		try {
			List<DataSource> dsks=dataSourceLoader.loadAll();
			
			if(null!=dsks){
				if(StringUtil.notEmpty(sortByString)){
					PropertyComparator.sort (dsks, new MutableSortDefinition ( "batchUid", true, true));
				}else{
					PropertyComparator.sort (dsks, new MutableSortDefinition ( sortByString.trim(), true, true));
				}
				if(null!=excludedPatterns){
					Collections.sort(excludedPatterns);
					String batchUid=null;
					for(String regex : excludedPatterns){
						List<DataSource> dsks2Removed=new LinkedList<DataSource>();
						if(StringUtil.notEmpty(regex)){
							for(DataSource dsk: dsks){					
								if(StringUtil.notEmpty(batchUid=dsk.getBatchUid())
									&& batchUid.matches(regex)){
									dsks2Removed.add(dsk);								
								}
							}
						}
						
						if(null!=dsks2Removed && dsks2Removed.size()>0){
							dsks.removeAll(dsks2Removed);
						}
					}
				}
				return Collections.unmodifiableList (dsks);
			}
		} catch (PersistenceException e) {
			logger.error("Method: getFilteredDataSourceKeys: "+e.getMessage());
		}
		return null;
	}
}
