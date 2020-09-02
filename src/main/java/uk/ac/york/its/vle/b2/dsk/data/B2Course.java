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

import blackboard.data.course.Course;

public class B2Course {
	
	//Object: to allow both Course and CourseSite to work.
	private Course course;
	private String courseId;
	private String courseBatchUid;
	private boolean isCourseDisabled;
	private String dataSourceKey;

	public B2Course(Course course){
		this.course = course;
		if(null!=course){
			this.courseId=course.getCourseId();
		}
	}


	public Course getCourse() {
		return course;
	}
	
	public String getCourseBatchUid() {
		return courseBatchUid;
	}


	public void setCourseBatchUid(String courseBatchUid) {
		this.courseBatchUid = courseBatchUid;
	}


	public void setCourse(Course course) {
		this.course = course;
	}
	
	public String getCourseId() {
		return courseId;
	}
	
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public boolean getIsCourseDisabled() {
		return isCourseDisabled;
	}
	
	public void setIsCourseDisabled(boolean isCourseDisabled) {
		this.isCourseDisabled = isCourseDisabled;
	}
	
	public String getDataSourceKey() {
		return dataSourceKey;
	}
	
	public void setDataSourceKey(String dataSourceKey) {
		this.dataSourceKey = dataSourceKey;
	}
}
