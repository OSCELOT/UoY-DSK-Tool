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

<fmt:message var="lDataSourceKey" key="inventory.list.label.data_source_key"/>
<fmt:message var="lAvailability" key="inventory.list.label.availability"/>
<fmt:message var="lRowStatus" key="inventory.list.label.row_status"/>
<fmt:message var="vEnabled" key="inventory.list.value.enabled"/>
<fmt:message var="vDisabled" key="inventory.list.value.disabled"/>
<fmt:message var="vAvailable" key="inventory.list.value.available"/>
<fmt:message var="vUnavailable" key="inventory.list.value.unavailable"/>
<fmt:message var="mCourseOrOrganisationInv" key="inventory.list.desc.course"/>

<c:choose>
    <c:when test="${title_view_id=='course_course'}">
        <fmt:message var="lCourseOrOrganisationId" key="inventory.list.label.course_id"/>
        <fmt:message var="lCourseOrOrganisationBatchUid" key="inventory.list.label.external_course_key"/>
        <fmt:message var="lCourseOrOrganisationTitle" key="inventory.list.label.course_title"/>
        <c:set var="vCourseOrOrganisationCollection" value="${COURSES}"/>
    </c:when>
    <c:when test="${title_view_id=='course_category'}">
        <fmt:message var="lCourseOrOrganisationId" key="inventory.list.label.course_id"/>
        <fmt:message var="lCourseOrOrganisationBatchUid" key="inventory.list.label.external_course_key"/>
        <fmt:message var="lCourseOrOrganisationTitle" key="inventory.list.label.course_title"/>
        <c:set var="vCourseOrOrganisationCollection" value="${COURSES}"/>
    </c:when>
    <c:when test="${title_view_id=='organisation_organisation'}">
        <fmt:message var="lCourseOrOrganisationId" key="inventory.list.label.organisation_id"/>
        <fmt:message var="lCourseOrOrganisationBatchUid" key="inventory.list.label.external_organisation_key"/>
        <fmt:message var="lCourseOrOrganisationTitle" key="inventory.list.label.organisation_title"/>
        <c:set var="vCourseOrOrganisationCollection" value="${ORGANISATIONS}"/>
    </c:when>
    <c:when test="${title_view_id=='organisation_category'}">
        <fmt:message var="lCourseOrOrganisationId" key="inventory.list.label.organisation_id"/>
        <fmt:message var="lCourseOrOrganisationBatchUid" key="inventory.list.label.external_organisation_key"/>
        <fmt:message var="lCourseOrOrganisationTitle" key="inventory.list.label.organisation_title"/>
        <c:set var="vCourseOrOrganisationCollection" value="${ORGANISATIONS}"/>
    </c:when>
    <c:otherwise>
        <fmt:message var="lCourseOrOrganisationId" key="inventory.list.label.course_id"/>
        <fmt:message var="lCourseOrOrganisationBatchUid" key="inventory.list.label.external_course_key"/>
        <fmt:message var="lCourseOrOrganisationTitle" key="inventory.list.label.course_title"/>
        <c:set var="vCourseOrOrganisationCollection" value="${COURSES}"/>
    </c:otherwise>
</c:choose>

<c:set var="_inventory_type" value="course" />
<c:if test="${not empty inventory_type}"><c:set var="_inventory_type" value="${inventory_type}" /></c:if>

<bbNG:inventoryList
        listId="listContainer"
        collection="${vCourseOrOrganisationCollection}"
        className="uk.ac.york.its.vle.b2.dsk.data.B2Course"
        description="${mCourseOrOrganisationInv}"
        objectVar="b2Course"
        enableSelectEntireList="true"
        includePageParameters="false"
        url="${pageContext.request.contextPath}/${_inventory_type}/${inventory_list_type}/list"
        initialSortCol="courseId"
>


    <c:choose>
        <c:when test="${not empty CHECKBOX_KEY}">
            <bbNG:listCheckboxElement name="${CHECKBOX_KEY}" value="${b2Course.courseId}"/>
        </c:when>
    </c:choose>

    <bbNG:listElement label="${lCourseOrOrganisationId}" name="courseId" isRowHeader="true"
                      comparator="${courseIdComparator}">
        ${b2Course.course.courseId}
    </bbNG:listElement>

    <bbNG:listElement label="${lCourseOrOrganisationTitle}" name="courseTitle" comparator="${courseTitleComparator}">
        ${b2Course.course.title}
    </bbNG:listElement>

    <bbNG:listElement label="Service Level" name="serviceLevelType" comparator="${courseTypeComparator}">
        <c:choose>
            <c:when test="${b2Course.course.serviceLevelType.fieldName == 'COMMUNITY'}">
                <c:out value="ORGANISATION"/>
            </c:when>
            <c:when test="${b2Course.course.serviceLevelType.fieldName == 'FULL'}">
                <c:out value="COURSE"/>
            </c:when>
            <c:otherwise>
                ${b2Course.course.serviceLevelType.fieldName}
            </c:otherwise>
        </c:choose>
    </bbNG:listElement>

    <bbNG:listElement label="${lRowStatus}" name="rowStatus" comparator="${rowStatusComparator}">
        <c:choose>
            <c:when test="${b2Course.isCourseDisabled == true}">
                <c:out value="${vDisabled}"/>
            </c:when>
            <c:when test="${b2Course.isCourseDisabled  == false}">
                <c:out value="${vEnabled}"/>
            </c:when>
        </c:choose>
    </bbNG:listElement>

    <bbNG:listElement label="${lAvailability}" name="isAvailable" comparator="${availabilityComparator}">
        <c:choose>
            <c:when test="${b2Course.course.isAvailable == true}">
                <c:out value="${vAvailable}"/>
            </c:when>
            <c:when test="${b2Course.course.isAvailable == false}">
                <c:out value="${vUnavailable}"/>
            </c:when>
        </c:choose>
    </bbNG:listElement>

    <bbNG:listElement label="${lCourseOrOrganisationBatchUid}" name="courseBatchUid" comparator="${batUidComparator}">
        ${b2Course.courseBatchUid}
    </bbNG:listElement>

    <bbNG:listElement label="${lDataSourceKey}" name="dataSourceBatchUid" comparator="${dataSourceKeyComparator}">
        ${b2Course.dataSourceKey}
    </bbNG:listElement>

</bbNG:inventoryList>