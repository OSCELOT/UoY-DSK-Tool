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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ include file="/WEB-INF/includes/include.jsp" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<fmt:message var="defaultPageTitle" key="b2.title"/>
<fmt:message var="breadcrumbTitle" key="breadcrumb.title.pesistence_outcome"/>

<fmt:message var="tFail" key="receipt.fail.title"/>
<fmt:message var="tSuccess" key="receipt.success.title"/>
<fmt:message var="tChangeComments" key="step.title.change.comments"/>
<fmt:message var="failReceiptType" key="receipt.fail.type"/>
<fmt:message var="successReceiptType" key="receipt.success.type"/>

<c:choose>

    <c:when test="${receiptType == failReceiptType}">
        <bbNG:genericPage authentication="Y" navItem="admin_main">
            <bbNG:receipt title="${tFail}" type="${failReceiptType}" recallUrl="/">
                <br/><tiles:insertAttribute name="body"/><br/>
            </bbNG:receipt>
        </bbNG:genericPage>
    </c:when>
    <c:when test="${receiptType == successReceiptType}">
        <bbNG:genericPage authentication="Y" navItem="admin_main" entitlement="system.plugin.MODIFY">
            <bbNG:pageHeader>
                <bbNG:pageTitleBar title="${defaultPageTitle}"/>
                <bbNG:breadcrumbBar environment="SYS_ADMIN" navItem="admin_main">
                    <bbNG:breadcrumb
                            href="${pageContext.request.contextPath}/user">${defaultPageTitle}</bbNG:breadcrumb>
                    <bbNG:breadcrumb>${breadcrumbTitle}</bbNG:breadcrumb>
                </bbNG:breadcrumbBar>
            </bbNG:pageHeader>
            <bbNG:receipt title="${tSuccess}" type="${receiptType}" recallUrl="${RECALL_URL}">
                <c:if test="${not empty changeComments}">
                    <h3>${tChangeComments}</h3>
                    <p class="helphelp">${changeComments}</p>
                    <br/>
                </c:if>
                <br/><tiles:insertAttribute name="body"/><br/>
            </bbNG:receipt>
        </bbNG:genericPage>
    </c:when>
</c:choose>

