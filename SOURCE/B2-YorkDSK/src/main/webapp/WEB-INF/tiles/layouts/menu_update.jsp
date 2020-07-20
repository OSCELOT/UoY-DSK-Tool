<%--

    Copyright (C) 2016 University of York, UK.

    This project was initiated through a donation of source code by the
    University of York, UK. It contains free software; you can redistribute
    it and/or modify it under the terms of the GNU General Public License as
    published by the Free Software Foundation; either version 2 of the
    License, or any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.

    For more information please contact:

    Web Services Group
    IT Service
    University of York
    YO10 5DD
    United Kingdom

--%>
<%@ include file="/WEB-INF/includes/include.jsp" %>
<c:choose>
	<c:when test="${title_view_id=='user'}">
		<c:set var="dataSourceKeysItems" value="${userDataSourceKeys}"/>
	</c:when>
	<c:when test="${title_view_id=='course'}">
		<c:set var="dataSourceKeysItems" value="${courseDataSourceKeys}"/>
	</c:when>
	<c:when test="${title_view_id=='course_course'}">
		<c:set var="dataSourceKeysItems" value="${courseDataSourceKeys}"/>
	</c:when>
		<c:when test="${title_view_id=='course_category'}">
		<c:set var="dataSourceKeysItems" value="${courseDataSourceKeys}"/>
	</c:when>	
	<c:when test="${title_view_id=='organisation'}">
		<c:set var="dataSourceKeysItems" value="${organisationDataSourceKeys}"/>
	</c:when>
	<c:when test="${title_view_id=='organisation_organisation'}">
		<c:set var="dataSourceKeysItems" value="${organisationDataSourceKeys}"/>
	</c:when>
	<c:when test="${title_view_id=='organisation_category'}">
		<c:set var="dataSourceKeysItems" value="${organisationDataSourceKeys}"/>
	</c:when>
	<c:when test="${title_view_id=='uenrolment'}">
		<c:set var="dataSourceKeysItems" value="${userEnrolmentDataSourceKeys}"/>	
	</c:when>
	<c:when test="${title_view_id=='cenrolment'}">
		<c:set var="dataSourceKeysItems" value="${courseEnrolmentDataSourceKeys}"/>	
	</c:when>
	<c:when test="${title_view_id=='oenrolment'}">
		<c:set var="dataSourceKeysItems" value="${courseEnrolmentDataSourceKeys}"/>	
	</c:when>
	<c:otherwise>
	    <c:set var="dataSourceKeysItems" value="${dataSourceKeys}"/>
	</c:otherwise>
</c:choose>		


<c:choose>
	<c:when test="${not empty USER_ENROLMENTS}">
		<c:set var="enabledCourseRoleDropdownMenu" value="true"/>
		<c:set var="rolesList" value="${courseOrOrganisationRoles}"/>
	</c:when>
	<c:when test="${not empty COURSE_ENROLMENTS}">
		<c:set var="enabledCourseRoleDropdownMenu" value="true"/>
		<c:set var="rolesList" value="${courseRoles}"/>
	</c:when>
	<c:when test="${not empty ORGANISATION_ENROLMENTS}">
		<c:set var="enabledCourseRoleDropdownMenu" value="true"/>
		<c:set var="rolesList" value="${organisationRoles}"/>
	</c:when>
	<c:otherwise>
			<c:set var="enabledCourseRoleDropdownMenu" value="false"/>
	</c:otherwise>
</c:choose>	

<fmt:message var="lIsUpdateRequired" key="form.checbox.label.is_update_required"/>
<table>
	<thead>
		<tr>
			<th/>
			<c:if test="${enabledCourseRoleDropdownMenu=='true'}">
				<th><form:checkbox path="isCourseRoleRequired"/> Role</th>
			</c:if>
			<th><form:checkbox path="isStatusUpdateRequired"/> Status</th>
			<th><form:checkbox path="isAvailabilityUpdateRequired"/> Availability</th>
            <th><form:checkbox path="isDataSourceKeyUpdateRequired"/>Data Source Key</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td><form:checkbox path="isUpdateRequired" label="${lIsUpdateRequired}"/></td>
			<c:if test="${enabledCourseRoleDropdownMenu=='true'}">
				<td>
					<form:select path="selectedCourseRole">
						<form:options items="${rolesList}" itemLabel="itemLabel" itemValue="itemValue"/>
					</form:select>
				</td>
			</c:if>
			<td>
				<form:select path="selectedRowStatus">
					<form:option label="Enabled" value="Enabled"/>
					<form:option label="Disabled" value="Disbaled"/>
				</form:select>
			</td>
			
			<td>
				<form:select path="selectedAvailability">
					<form:option label="Available" value="Available"/>
					<form:option label="Unavailable" value="Unavailable"/>
				</form:select>
			</td>
			<td><form:select path="selectedDataSourceKey" items="${dataSourceKeysItems}" itemLabel="batchUid" itemValue="batchUid"/></td>
		</tr>
	</tbody>
</table>
<br/><br/>