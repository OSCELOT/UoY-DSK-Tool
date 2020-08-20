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

import blackboard.admin.data.course.Enrollment;

/**
 * @author Kelvin Hai {@link <a href="mailto:kelvin.hai@york.ac.uk">kelvin.hai@york.ac.uk</a>}
 * @version $Revision$ $Date$
 */

public class B2Enrolment {

	public static final String USER_ENROLMENT_TYPE = "user";
	public static final String COURSE_ENROLMENT_TYPE = "course";
	public static final String ORGANISATION_ENROLMENT_TYPE = "organisation";

	private String id;
	private String courseId;
	private String courseTitle;
	private String userId;
	private String givenName;
	private String familyName;
	private String category;
	private String serviceLevel;
	private String role;
	private Enrollment enrollment;
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * @return the courseId
	 */
	public String getCourseId() {
		return courseId;
	}
	
	/**
	 * @param courseId the courseId to set
	 */
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	
	/**
	 * @return the courseTitle
	 */
	public String getCourseTitle() {
		return courseTitle;
	}
	
	/**
	 * @param courseTitle the courseTitle to set
	 */
	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}
	
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	

	/**
	 * @return the givenName
	 */
	public String getGivenName() {
		return givenName;
	}

	/**
	 * @param givenName the givenName to set
	 */
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	/**
	 * @return the familyName
	 */
	public String getFamilyName() {
		return familyName;
	}

	/**
	 * @param familyName the familyName to set
	 */
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
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

	public String getServiceLevel() {
		return serviceLevel;
	}

	public void setServiceLevel(String serviceLevel) {
		this.serviceLevel = serviceLevel;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * @return the enrollment
	 */
	public Enrollment getEnrollment() {
		return enrollment;
	}
	
	/**
	 * @param enrollment the enrollment to set
	 */
	public void setEnrollment(Enrollment enrollment) {
		this.enrollment = enrollment;
	}
}
