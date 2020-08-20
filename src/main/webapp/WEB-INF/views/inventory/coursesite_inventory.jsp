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

<fmt:message var="lCourseId" key="inventory.list.label.course_id"/>
<fmt:message var="lCourseTitle" key="inventory.list.label.course_title"/>
<fmt:message var="lDataSourceKey" key="inventory.list.label.data_source_key"/>
<fmt:message var="lAvailability" key="inventory.list.label.availability"/>
<fmt:message var="lRowStatus" key="inventory.list.label.row_status"/>
<fmt:message var="vEnabled" key="inventory.list.value.enabled"/>
<fmt:message var="vDisabled" key="inventory.list.value.disabled"/>
<fmt:message var="vAvailable" key="inventory.list.value.available"/>
<fmt:message var="vUnavailable" key="inventory.list.value.unavailable"/>
<bbNG:inventoryList
	listId="listContainer"
	collection="${COURSES}" 
	className="blackboard.admin.data.course.CourseSite"
	initialSortCol="courseId" 
	description="${dCourseInv}" 
	objectVar="course" 
	limitMaxNumOfItems="true" 
	enableSelectEntireList="true"
	showAll="true"
	includePageParameters="false"
	>


	<c:choose>
		<c:when test="${not empty CHECKBOX_KEY}">
			<bbNG:listCheckboxElement name="${CHECKBOX_KEY}" value="${course.batchUid}"/>
		</c:when>
	</c:choose>
    
   <bbNG:listElement label="${lCourseId}" name="courseId" isRowHeader="true">
      ${course.courseId}
    </bbNG:listElement>
    
    <bbNG:listElement label="${lCourseTitle}" name="courseTitle">
      ${course.title}
    </bbNG:listElement>
    
    <bbNG:listElement label="${lRowStatus}" name="rowStatus">
      ${course.rowStatus.fieldName}
    </bbNG:listElement>
    
    <bbNG:listElement label="${lAvailability}" name="isAvailable">
    	<c:choose>
    		<c:when test="${course.isAvailable == true}">
    			<c:out value="${vAvailable}"/>
    		</c:when>
    		<c:when test="${course.isAvailable == false}">
    			<c:out value="${vUnavailable}"/>
    		</c:when>
    	</c:choose>
    </bbNG:listElement>
    
    <bbNG:listElement label="${lDataSourceKey}" name="dataSourceBatchUid">
      ${course.dataSourceBatchUid}
    </bbNG:listElement>
    
</bbNG:inventoryList>