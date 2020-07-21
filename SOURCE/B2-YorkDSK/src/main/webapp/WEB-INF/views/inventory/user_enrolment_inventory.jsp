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

<c:set var="enableSelectEntireList" value="true"/>

<fmt:message var="lExternalCourseKey" key="inventory.list.label.external_course_organisation_key"/>
<fmt:message var="lCategory" key="inventory.list.label.category"/>
<fmt:message var="lCourseId" key="inventory.list.label.course_id"/>
<fmt:message var="lCourseTitle" key="inventory.list.label.course_title"/>
<fmt:message var="lRole" key="inventory.list.label.role"/>
<fmt:message var="lDataSourceKey" key="inventory.list.label.data_source_key"/>
<fmt:message var="lAvailability" key="inventory.list.label.availability"/>
<fmt:message var="lRowStatus" key="inventory.list.label.row_status"/>
<fmt:message var="vEnabled" key="inventory.list.value.enabled"/>
<fmt:message var="vDisabled" key="inventory.list.value.disabled"/>
<fmt:message var="vAvailable" key="inventory.list.value.available"/>
<fmt:message var="vUnavailable" key="inventory.list.value.unavailable"/>
<fmt:message var="dUserEnrolInv" key="inventory.list.desc.user_enrolment"/>

<bbNG:inventoryList
        listId="listContainer"
        collection="${USER_ENROLMENTS}"
        className="uk.ac.york.its.vle.b2.dsk.data.B2Enrolment"
        initialSortCol="courseSiteBatchUid"
        description="${dUserEnrolInv}"
        objectVar="b2enrolment"
        limitMaxNumOfItems="true"
        enableSelectEntireList="${enableSelectEntireList}"
        includePageParameters="false"
        url="${pageContext.request.contextPath}/enrolment/user/list"
>

    <c:choose>
        <c:when test="${not empty CHECKBOX_KEY}">
            <bbNG:listCheckboxElement name="${CHECKBOX_KEY}" value="${b2enrolment.enrollment.id.externalString}"/>
        </c:when>
    </c:choose>

    <bbNG:listElement label="${lExternalCourseKey}" name="courseSiteBatchUid" isRowHeader="true"
                      comparator="${courseBatchUidComparator}">
        ${b2enrolment.enrollment.courseSiteBatchUid}
    </bbNG:listElement>

    <bbNG:listElement label="${lCourseId}" name="courseId" comparator="${courseIdComparator}">
        ${b2enrolment.courseId}
    </bbNG:listElement>

    <bbNG:listElement label="${lCourseTitle}" name="courseTitle" comparator="${courseTitleComparator}">
        ${b2enrolment.courseTitle}
    </bbNG:listElement>

    <bbNG:listElement label="Service Level" name="serviceLevel" comparator="${courseTypeComparator}">
        <c:choose>
            <c:when test="${b2enrolment.serviceLevel == 'COMMUNITY'}">
                <c:out value="ORGANISATION"/>
            </c:when>
            <c:when test="${b2enrolment.serviceLevel == 'FULL'}">
                <c:out value="COURSE"/>
            </c:when>
            <c:otherwise>
                ${b2enrolment.serviceLevel}
            </c:otherwise>
        </c:choose>
    </bbNG:listElement>

    <c:if test="${not empty b2enrolment.category}">
        <bbNG:listElement label="${lCategory}" name="category" comparator="${categoryComparator}">
            ${b2enrolment.category}
        </bbNG:listElement>
    </c:if>

    <bbNG:listElement label="${lRole}" name="role" comparator="${roleComparator}">
        ${b2enrolment.role}
    </bbNG:listElement>

    <bbNG:listElement label="${lRowStatus}" name="rowStatus" comparator="${rowStatusComparator}">
        ${b2enrolment.enrollment.rowStatus.fieldName}
    </bbNG:listElement>

    <bbNG:listElement label="${lAvailability}" name="isAvailable" comparator="${availabilityComparator}">
        <c:choose>
            <c:when test="${b2enrolment.enrollment.isAvailable == true}">
                <c:out value="${vAvailable}"/>
            </c:when>
            <c:when test="${b2enrolment.enrollment.isAvailable == false}">
                <c:out value="${vUnavailable}"/>
            </c:when>
        </c:choose>
    </bbNG:listElement>

    <bbNG:listElement label="${lDataSourceKey}" name="dataSourceBatchUid" comparator="${dataSourceKeyComparator}">
        ${b2enrolment.enrollment.dataSourceBatchUid}
    </bbNG:listElement>

</bbNG:inventoryList>