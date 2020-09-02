<%@ include file="/WEB-INF/includes/include.jsp" %>

<fmt:message var="lDataSourceKey" key="inventory.list.label.data_source_key"/>
<fmt:message var="lAvailability" key="inventory.list.label.availability"/>
<fmt:message var="lRowStatus" key="inventory.list.label.row_status"/>
<fmt:message var="lOutcome" key="inventory.list.label.outcome"/>
<c:choose>
    <c:when test="${title_view_id=='success_course'}">
        <fmt:message var="lCourseOrOrganisationId" key="inventory.list.label.course_id"/>
        <fmt:message var="lCourseOrOrganisationBatchUid" key="inventory.list.label.external_course_key"/>
        <fmt:message var="lCourseOrOrganisationTitle" key="inventory.list.label.course_title"/>
    </c:when>
    <c:when test="${title_view_id=='success_organisation'}">
        <fmt:message var="lCourseOrOrganisationId" key="inventory.list.label.organisation_id"/>
        <fmt:message var="lCourseOrOrganisationBatchUid" key="inventory.list.label.external_organisation_key"/>
        <fmt:message var="lCourseOrOrganisationTitle" key="inventory.list.label.organisation_title"/>
    </c:when>
    <c:otherwise>
        <fmt:message var="lCourseOrOrganisationId" key="inventory.list.label.course_id"/>
        <fmt:message var="lCourseOrOrganisationBatchUid" key="inventory.list.label.external_course_key"/>
        <fmt:message var="lCourseOrOrganisationTitle" key="inventory.list.label.course_title"/>
    </c:otherwise>
</c:choose>


<bbNG:inventoryList collection="${outcomes}"
                    className="uk.ac.york.its.vle.b2.dsk.model.PersistenceOutcome"
                    objectVar="outcome"
                    showAll="true">

    <bbNG:listElement label="${lCourseOrOrganisationBatchUid}" name="courseBatchUid" isRowHeader="true">
        ${outcome.courseBatchUid}
    </bbNG:listElement>

    <bbNG:listElement label="${lCourseOrOrganisationId}" name="courseId">
        ${outcome.courseId}
    </bbNG:listElement>

    <bbNG:listElement label="${lCourseOrOrganisationTitle}" name="courseTitle">
        ${outcome.courseTitle}
    </bbNG:listElement>


    <bbNG:listElement label="${lRowStatus}" name="rowStatus">
        ${outcome.rowStatus}
    </bbNG:listElement>

    <bbNG:listElement label="${lAvailability}" name="isAvailable">
        ${outcome.availability}
    </bbNG:listElement>

    <bbNG:listElement label="${lDataSourceKey}" name="dataSourceKey">
        ${outcome.dataSourceKey}
    </bbNG:listElement>

    <bbNG:listElement label="${lOutcome}" name="outcomeStatus">
        <c:choose>
            <c:when test="${outcome.isPersistedSuccess == true}">
                <p style="color : green;">
                    <strong><fmt:message key="persist.message.success"/></strong>
                </p>
            </c:when>
            <c:when test="${outcome.isBeforeUpdate == true}">
                <strong><fmt:message key="persist.message.before.update"/></strong>
            </c:when>
            <c:otherwise>
                <p style="color : red;">
                    <strong><fmt:message key="persist.message.failed"/></strong>
                </p>
            </c:otherwise>
        </c:choose>
    </bbNG:listElement>

</bbNG:inventoryList>
