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

package uk.ac.york.its.vle.b2.dsk.model;

import blackboard.data.AbstractIdentifiable;
import blackboard.persist.DataType;
import blackboard.persist.Id;
import blackboard.persist.impl.mapping.annotation.Column;
import blackboard.persist.impl.mapping.annotation.PrimaryKey;
import blackboard.persist.impl.mapping.annotation.RefersTo;
import blackboard.persist.impl.mapping.annotation.Table;
import blackboard.util.StringUtil;
import uk.ac.york.its.vle.b2.dsk.data.B2Enum;

/**
 * @author Kelvin Hai {@link <a href="mailto:kelvin.hai@york.ac.uk">kelvin.hai@york.ac.uk</a>}
 * @version $Revision$ $Date$
 */

@Table("york_dsk_change_log")
public class PersistenceOutcome  extends AbstractIdentifiable {
    private static final String DEFAULT_DELIMITER = "|";
    private static final String DEFAULT_HEADER = "UserBatchUid|UserName|GivenName|FamilyName|CourseBatchUid|CourseId|CourseTitle|CourseRole|DataSourceKey|RowStatus|Availability|IsPersistedSuccess";
    private String delimiter = DEFAULT_DELIMITER;
    private String header = DEFAULT_HEADER;

    public static final DataType DATA_TYPE = new DataType(PersistenceOutcome.class);

    public DataType getDataType() {
        return DATA_TYPE;
    }

    @PrimaryKey
    private int pk1;

    @Column({"york_dsk_change_message_pk1"})
    @RefersTo(ChangeMessage.class)
    private Id changeMessageId;
    @Column({"external_person_key"})
    private String userBatchUid;
    @Column({"user_id"})
    private String userName;
    @Column({"firstname"})
    private String givenName;
    @Column({"lastname"})
    private String familyName;
    @Column({"external_course_key"})
    private String courseBatchUid;
    @Column({"course_id"})
    private String courseId;
    @Column({"course_title"})
    private String courseTitle;
    @Column({"course_role"})
    private String courseRole;
    @Column({"data_source_key"})
    private String dataSourceKey;
    @Column({"row_status"})
    private String rowStatus;
    @Column({"available_ind"})
    private boolean isAvailable;
    @Column({"success_ind"})
    private boolean isPersistedSuccess = false;
    @Column({"origin_ind"})
    private boolean isBeforeUpdate = false;
    private String errorMessage;
    private String availability;

    public Id getChangeMessageId() {
        return changeMessageId;
    }

    public void setChangeMessageId(Id changeMessageId) {
        this.changeMessageId = changeMessageId;
    }

    public String getUserBatchUid() {
        return userBatchUid;
    }

    public void setUserBatchUid(String userBatchUid) {
        this.userBatchUid = userBatchUid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getCourseBatchUid() {
        return courseBatchUid;
    }

    public void setCourseBatchUid(String courseBatchUid) {
        this.courseBatchUid = courseBatchUid;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseRole() {
        return courseRole;
    }

    public void setCourseRole(String courseRole) {
        this.courseRole = courseRole;
    }

    public String getDataSourceKey() {
        return dataSourceKey;
    }

    public void setDataSourceKey(String dataSourceKey) {
        this.dataSourceKey = dataSourceKey;
    }

    public String getRowStatus() {
        return rowStatus;
    }

    public void setRowStatus(String rowStatus) {
        this.rowStatus = rowStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        if(isAvailable){
            setAvailability(B2Enum.Available.getName());
        }else{
            setAvailability(B2Enum.Unavailable.getName());
        }
        this.isAvailable = isAvailable;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public boolean getIsPersistedSuccess() {
        return isPersistedSuccess;
    }

    public void setIsPersistedSuccess(boolean isPersistedSuccess) {
        this.isPersistedSuccess = isPersistedSuccess;
    }

    public boolean getIsBeforeUpdate() {
        return isBeforeUpdate;
    }

    public void setIsBeforeUpdate(boolean isBeforeUpdate) {
        this.isBeforeUpdate = isBeforeUpdate;
    }

    public String getDelimiter() { return delimiter; }

    public void setDelimiter(String delimiter) {
        if(StringUtil.isEmpty(delimiter)){
            delimiter=DEFAULT_DELIMITER;
        }
        this.delimiter = delimiter;
    }

    public String getHeader() {
        if(!delimiter.equals(DEFAULT_DELIMITER)){
            StringBuffer sb = new StringBuffer();
            sb.append("UserBatchUid").append(delimiter);
            sb.append("UserName").append(delimiter);
            sb.append("GivenName").append(delimiter);
            sb.append("FamilyName").append(delimiter);
            sb.append("CourseBatchUid").append(delimiter);
            sb.append("CourseId").append(delimiter);
            sb.append("CourseTitle").append(delimiter);
            sb.append("CourseRole").append(delimiter);
            sb.append("DataSourceKey").append(delimiter);
            sb.append("RowStatus").append(delimiter);
            sb.append("Availability").append(delimiter);
            sb.append("IsPersistedSuccess");
            return sb.toString();
        }
        return header;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(userBatchUid).append(delimiter);
        sb.append(userName).append(delimiter);
        sb.append(givenName).append(delimiter);
        sb.append(familyName).append(delimiter);
        sb.append(courseBatchUid).append(delimiter);
        sb.append(courseId).append(delimiter);
        sb.append(courseTitle).append(delimiter);
        sb.append(courseRole).append(delimiter);
        sb.append(dataSourceKey).append(delimiter);
        sb.append(rowStatus).append(delimiter);
        sb.append(availability).append(delimiter);
        sb.append(isPersistedSuccess);
        return sb.toString();
    }
}