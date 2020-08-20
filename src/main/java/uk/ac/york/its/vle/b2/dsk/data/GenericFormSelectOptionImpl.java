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

import blackboard.admin.data.category.CourseCategory;
import blackboard.admin.data.category.OrganizationCategory;
import blackboard.data.course.CourseMembership;
import blackboard.persist.SearchOperator;
import blackboard.persist.course.CourseSearch;
import blackboard.persist.user.UserSearch;


/**
 * @author Kelvin Hai {@link <a href="mailto:kelvin.hai@york.ac.uk">kelvin.hai@york.ac.uk</a>}
 * @version $Revision$ $Date$
 */
public class GenericFormSelectOptionImpl implements GenericFormSelectOption{

	private String key;
	private String value;
	
	public GenericFormSelectOptionImpl(Object object){
		populate(object);
	}
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	//Alias to the getValue() method
	public String getItemLabel() {
		return value;
	}

	//Alias to the getKey() method
	public String getItemValue() {
		return key;
	}
	
	private void populate(Object object){
		if(null!=object){
			if(object.getClass().getName().equals(CourseSearch.SearchKey.class.getName())){
				CourseSearch.SearchKey sk=(CourseSearch.SearchKey)object;
				key=sk.name();
				value=key;
			}else if(object.getClass().getName().equals(UserSearch.SearchKey.class.getName())){
				// can be achieved without using GenericReferenceMapImpl because proper getter existed i.e. getName
				UserSearch.SearchKey sk=(UserSearch.SearchKey)object;
				key=sk.getName();
				value=key;	
			}else if(object.getClass().getName().equals(SearchOperator.class.getName())){
				// can be achieved without using GenericReferenceMapImpl because proper getter existed i.e. getLabel
				SearchOperator so=(SearchOperator)object;
				key=so.name();
				value=so.getLabel();
			}else if(object.getClass().getName().equals(CourseMembership.Role.class.getName())){
				// can be achieved without using GenericReferenceMapImpl because proper getter existed i.e. getFieldName
				CourseMembership.Role role=(CourseMembership.Role)object;
				key=role.getFieldName();
				value=key;
			}else if(object.getClass().getName().equals(CourseCategory.class.getName())){
				CourseCategory category=(CourseCategory)object;
				key=category.getBatchUid();
				value=""+category.getBatchUid()+" ("+category.getTitle()+")";
			}else if(object.getClass().getName().equals(OrganizationCategory.class.getName())){
				OrganizationCategory category=(OrganizationCategory)object;
				key=category.getBatchUid();
				value=""+category.getBatchUid()+" ("+category.getTitle()+")";
			}
		}
	}
	
}
