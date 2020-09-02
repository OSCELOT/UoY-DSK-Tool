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

<fmt:message var="lBatchUid" key="inventory.list.label.external_person_key"/>
<fmt:message var="lUserName" key="inventory.list.label.user_name"/>
<fmt:message var="lGivenName" key="inventory.list.label.given_name"/>
<fmt:message var="lFamilyName" key="inventory.list.label.family_name"/>
<fmt:message var="lEmailAddress" key="inventory.list.label.email_address"/>
<fmt:message var="lDataSourceKey" key="inventory.list.label.data_source_key"/>
<fmt:message var="lAvailability" key="inventory.list.label.availability"/>
<fmt:message var="lRowStatus" key="inventory.list.label.row_status"/>
<fmt:message var="vEnabled" key="inventory.list.value.enabled"/>
<fmt:message var="vDisabled" key="inventory.list.value.disabled"/>
<fmt:message var="vAvailable" key="inventory.list.value.available"/>
<fmt:message var="vUnavailable" key="inventory.list.value.unavailable"/>
<fmt:message var="dUserInv" key="inventory.list.desc.user"/>

<bbNG:inventoryList
        listId="listContainer"
        collection="${USERS}"
        className="uk.ac.york.its.vle.b2.dsk.data.B2User"
        url="${pageContext.request.contextPath}/user_list"
        initialSortCol="userName"
        description="${dUserInv}"
        objectVar="b2user"
        limitMaxNumOfItems="true"
        enableSelectEntireList="true"
        includePageParameters="false">

    <c:choose>
        <c:when test="${not empty CHECKBOX_KEY}">
            <bbNG:listCheckboxElement name="${CHECKBOX_KEY}" value="${b2user.userName}"/>
        </c:when>
        <c:when test="${not empty RADIO_KEY}">
            <bbNG:listRadioElement name="${RADIO_KEY}" value="${b2user.userName}"/>
        </c:when>
    </c:choose>

    <bbNG:listElement label="${lUserName}" name="userName" isRowHeader="true" comparator="${comparatorByUserName}">
        ${b2user.userName}
    </bbNG:listElement>

    <bbNG:listElement label="${lGivenName}" name="givenName" comparator="${givenNameComparator}">
        ${b2user.user.givenName}
    </bbNG:listElement>

    <bbNG:listElement label="${lFamilyName}" name="familyName" comparator="${familyNameComparator}">
        ${b2user.user.familyName}
    </bbNG:listElement>

    <bbNG:listElement label="${lEmailAddress}" name="emailAddress" comparator="${emailAddressComparator}">
        ${b2user.user.emailAddress}
    </bbNG:listElement>

    <bbNG:listElement label="${lRowStatus}" name="rowStatus" comparator="${rowStatusComparator}">
        <c:choose>
            <c:when test="${b2user.isUserDisabled == true}">
                <c:out value="${vDisabled}"/>
            </c:when>
            <c:when test="${b2user.isUserDisabled == false}">
                <c:out value="${vEnabled}"/>
            </c:when>
        </c:choose>
    </bbNG:listElement>

    <bbNG:listElement label="${lAvailability}" name="isAvailable" comparator="${availabilityComparator}">
        <c:choose>
            <c:when test="${b2user.user.isAvailable == true}">
                <c:out value="${vAvailable}"/>
            </c:when>
            <c:when test="${b2user.user.isAvailable == false}">
                <c:out value="${vUnavailable}"/>
            </c:when>
        </c:choose>
    </bbNG:listElement>

    <bbNG:listElement label="${lBatchUid}" name="batchUid" comparator="${batUidComparator}">
        ${b2user.user.batchUid}
    </bbNG:listElement>

    <bbNG:listElement label="${lDataSourceKey}" name="dataSourceKey" comparator="${dataSourceKeyComparator}">
        ${b2user.dataSourceKey}
    </bbNG:listElement>

</bbNG:inventoryList>