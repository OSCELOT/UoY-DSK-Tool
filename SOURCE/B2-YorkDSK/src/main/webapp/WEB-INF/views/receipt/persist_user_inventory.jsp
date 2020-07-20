<%@ include file="/WEB-INF/includes/include.jsp" %>

<fmt:message var="lUserName" key="inventory.list.label.user_name"/>
<fmt:message var="lGivenName" key="inventory.list.label.given_name"/>
<fmt:message var="lFamilyName" key="inventory.list.label.family_name"/>
<fmt:message var="lEmailAddress" key="inventory.list.label.email_address"/>
<fmt:message var="lDataSourceKey" key="inventory.list.label.data_source_key"/>
<fmt:message var="lAvailability" key="inventory.list.label.availability"/>
<fmt:message var="lRowStatus" key="inventory.list.label.row_status"/>
<fmt:message var="lOutcome" key="inventory.list.label.outcome"/>
<fmt:message var="vEnabled" key="inventory.list.value.enabled"/>
<fmt:message var="vDisabled" key="inventory.list.value.disabled"/>
<fmt:message var="vAvailable" key="inventory.list.value.available"/>
<fmt:message var="vUnavailable" key="inventory.list.value.unavailable"/>
<fmt:message var="desc" key="inventory.list.desc.uers_persistence"/>

<bbNG:inventoryList collection="${outcomes}"
                    className="uk.ac.york.its.vle.b2.dsk.model.PersistenceOutcome"
                    objectVar="outcome"
                    showAll="true">
    <bbNG:listElement label="${lUserName}" name="userName" isRowHeader="true">
        ${outcome.userName}
    </bbNG:listElement>

    <bbNG:listElement label="${lGivenName}" name="givenName">
        ${outcome.givenName}
    </bbNG:listElement>

    <bbNG:listElement label="${lFamilyName}" name="familyName">
        ${outcome.familyName}
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
