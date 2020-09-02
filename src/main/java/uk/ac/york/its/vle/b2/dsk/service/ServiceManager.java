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
package uk.ac.york.its.vle.b2.dsk.service;

import java.util.List;

import blackboard.admin.data.course.CourseSite;
import blackboard.admin.data.course.Enrollment;
import blackboard.admin.data.datasource.DataSource;
import blackboard.admin.data.user.Person;
import blackboard.data.course.Course;
import blackboard.data.user.User;
import uk.ac.york.its.vle.b2.dsk.model.PersistenceOutcome;


/**
 * @author Kelvin Hai {@link <a href="mailto:kelvin.hai@york.ac.uk">kelvin.hai@york.ac.uk</a>}
 * @version $Revision$ $Date$
 */

public interface ServiceManager {
    List<User> sortUsers(List<User> users, String objectPropertyName);
    List<Course> sortCourses(List<Course> courses, String objectPropertyName);
    List<Enrollment> sortEnrolments(List<Enrollment> enrolments, String objectPropertyName);
    CourseSite getCourseSiteByCourseSiteId(String courseSiteId);
    CourseSite getCourseSiteByBatchUid(String courseSiteBatchUid);
    DataSource getDataSourceByBatchUid(String dataSourceBatchUid);
    Person getPersonByUserName(String userName);
    void persistOutcome(String dskType, String changeComments, List<PersistenceOutcome> persistenceOutcomes);
}
