<%@ include file="/WEB-INF/includes/include.jsp" %>

<fmt:message var="lRole" key="inventory.list.label.role"/>
<fmt:message var="lExternalPersonKey" key="inventory.list.label.external_person_key"/>
<fmt:message var="lDataSourceKey" key="inventory.list.label.data_source_key"/>
<fmt:message var="lErrorMessage" key="inventory.list.label.error_message"/>
<fmt:message var="lAvailability" key="inventory.list.label.availability"/>
<fmt:message var="lRowStatus" key="inventory.list.label.row_status"/>
<fmt:message var="lOutcome" key="inventory.list.label.outcome"/>
<fmt:message var="desc" key="inventory.list.desc.enrolment_persistence"/>
<c:choose>
    <c:when test="${title_view_id=='success_course_enrolment'}">
        <fmt:message var="lExternalCourseKey" key="inventory.list.label.external_course_key"/>
    </c:when>
    <c:when test="${title_view_id=='success_organisation_enrolment'}">
        <fmt:message var="lExternalCourseKey" key="inventory.list.label.external_organisation_key"/>
    </c:when>
    <c:otherwise>
        <fmt:message var="lExternalCourseKey" key="inventory.list.label.external_course_organisation_key"/>
    </c:otherwise>
</c:choose>

<bbNG:inventoryList collection="${outcomes}"
                    className="uk.ac.york.its.vle.b2.dsk.model.PersistenceOutcome"
                    objectVar="outcome"
                    showAll="true">

    <bbNG:listElement label="${lExternalPersonKey}" name="userBatchUid" isRowHeader="true">
        ${outcome.userBatchUid}
    </bbNG:listElement>

    <bbNG:listElement label="${lExternalCourseKey}" name="courseBatchUId">
        ${outcome.courseBatchUid}
    </bbNG:listElement>

    <bbNG:listElement label="${lRole}" name="role">
        ${outcome.courseRole}
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

    <bbNG:listElement label="${lErrorMessage}" name="errorMessage">
        ${outcome.errorMessage}
    </bbNG:listElement>
</bbNG:inventoryList>
