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
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<fmt:message var="defaultPageTitle" key="b2.title"/>
<fmt:message var="tUpdate" key="step.title.update"/>
<fmt:message var="updateStepInstruction" key="step.search.instruction.default"/>
<fmt:message var="tChangeComment" key="step.title.change.comments"/>
<fmt:message var="changeCommentsStepInstruction" key="step.change.comments.instruction.default"/>


<!-- Search Bar Area-->
<c:choose>
	<c:when test="${title_view_id=='user'}">	    
		<fmt:message var="uPageTitle" key="page.title.user"/>
		<c:set var="pageTitle" value="${defaultPageTitle} : ${uPageTitle}"/>
		<fmt:message var="breadcrumbTitle" key="breadcrumb.title.user"/>
		<fmt:message var="searchTitle" key="step.search.title.user"/>
		<fmt:message var="sInstruction" key="step.search.instruction.user"/>
		<fmt:message var="resultDetails" key="step.result.title.user"/>
		<c:set var="formActionUri" value="${pageContext.request.contextPath}/user"/>
	</c:when>
	
	<c:when test="${title_view_id=='course'}">
		<fmt:message var="pageTitle" key="page.title.course_by_course_search"/>
		<fmt:message var="breadcrumbTitle" key="breadcrumb.title.course_by_course_search"/>
		<fmt:message var="searchTitle" key="search.title.course"/>
		<fmt:message var="sInstruction" key="step.search.instruction.course_by_course_search"/>
		<fmt:message var="resultDetails" key="step.result.title.course_by_course_search"/>
		<c:set var="formActionUri" value="${pageContext.request.contextPath}/course"/>
	</c:when>
	
	<c:when test="${title_view_id=='course_course'}">
		<fmt:message var="pageTitle" key="page.title.course_by_course_search"/>
		<fmt:message var="breadcrumbTitle" key="breadcrumb.title.course_by_course_search"/>
		<fmt:message var="searchTitle" key="step.search.title.course_by_course_search"/>
		<fmt:message var="sInstruction" key="step.search.instruction.course_by_course_search"/>
		<fmt:message var="resultDetails" key="step.result.title.course_by_course_search"/>
		<c:set var="formActionUri" value="${pageContext.request.contextPath}/course/course"/>
	</c:when>
	
	<c:when test="${title_view_id=='course_category'}">
		<fmt:message var="pageTitle" key="page.title.course_by_category_search"/>
		<fmt:message var="breadcrumbTitle" key="breadcrumb.title.course_by_category_search"/>
		<fmt:message var="searchTitle" key="step.search.title.course_by_category_search"/>
		<fmt:message var="sInstruction" key="step.search.instruction.course_by_category_search"/>
		<fmt:message var="resultDetails" key="step.result.title.course_by_category_search"/>
		<c:set var="formActionUri" value="${pageContext.request.contextPath}/course/category"/>
	</c:when>

	<c:when test="${title_view_id=='organisation'}">
		<fmt:message var="pageTitle" key="page.title.course_by_course_search"/>
		<fmt:message var="breadcrumbTitle" key="breadcrumb.title.course_by_course_search"/>
		<fmt:message var="searchTitle" key="search.title.course"/>
		<fmt:message var="sInstruction" key="step.search.instruction.course_by_course_search"/>
		<fmt:message var="resultDetails" key="step.result.title.course_by_course_search"/>
		<c:set var="formActionUri" value="${pageContext.request.contextPath}/organisation"/>
	</c:when>
	
	<c:when test="${title_view_id=='organisation_organisation'}">
		<fmt:message var="pageTitle" key="page.title.organisation_by_organisation_search"/>
		<fmt:message var="breadcrumbTitle" key="breadcrumb.title.organisation_by_organisation_search"/>
		<fmt:message var="searchTitle" key="step.search.title.organisation_by_organisation_search"/>
		<fmt:message var="sInstruction" key="step.search.instruction.organisation_by_organisation_search"/>
		<fmt:message var="resultDetails" key="step.result.title.organisation_by_organisation_search"/>
		<c:set var="formActionUri" value="${pageContext.request.contextPath}/organisation/organisation"/>
	</c:when>
	
	<c:when test="${title_view_id=='organisation_category'}">
		<fmt:message var="pageTitle" key="page.title.organisation_by_category_search"/>
		<fmt:message var="breadcrumbTitle" key="breadcrumb.title.organisation_by_category_search"/>
		<fmt:message var="searchTitle" key="step.search.title.organisation_by_category_search"/>
		<fmt:message var="sInstruction" key="step.search.instruction.organisation_by_category_search"/>
		<fmt:message var="resultDetails" key="step.result.title.organisation_by_category_search"/>
		<c:set var="formActionUri" value="${pageContext.request.contextPath}/organisation/category"/>
	</c:when>	
	
	<c:when test="${title_view_id=='cenrolment'}">
		<fmt:message var="pageTitle" key="page.title.enrolment_by_course_search"/>
		<fmt:message var="breadcrumbTitle" key="breadcrumb.title.enrolment_by_course_search"/>
		<fmt:message var="searchTitle" key="step.search.title.enrolment_by_course_search"/>
		<fmt:message var="sInstruction" key="step.search.instruction.enrolment_by_course_search"/>
		<fmt:message var="resultDetails" key="step.result.title.enrolment_by_course_search"/>
		<c:set var="formActionUri" value="${pageContext.request.contextPath}/enrolment/course"/>
	</c:when>
	
	<c:when test="${title_view_id=='oenrolment'}">
		<fmt:message var="pageTitle" key="page.title.enrolment_by_organisation_search"/>
		<fmt:message var="breadcrumbTitle" key="breadcrumb.title.enrolment_by_organisation_search"/>
		<fmt:message var="searchTitle" key="step.search.title.enrolment_by_organisation_search"/>
		<fmt:message var="sInstruction" key="step.search.instruction.enrolment_by_organisation_search"/>
		<fmt:message var="resultDetails" key="step.result.title.enrolment_by_organisation_search"/>
		<c:set var="formActionUri" value="${pageContext.request.contextPath}/enrolment/organisation"/>
	</c:when>
	
	<c:when test="${title_view_id=='uenrolment'}">
		<fmt:message var="pageTitle" key="page.title.enrolment_by_user_search"/>
		<fmt:message var="breadcrumbTitle" key="breadcrumb.title.enrolment_by_user_search"/>
		<fmt:message var="searchTitle" key="step.search.title.enrolment_by_user_search"/>	
		<fmt:message var="sInstruction" key="step.search.instruction.enrolment_by_user_search"/>
		<fmt:message var="resultDetails" key="step.result.title.enrolment_by_user_search"/>
		<c:set var="formActionUri" value="${pageContext.request.contextPath}/enrolment/user"/>
	</c:when>
	
	<c:otherwise>
		<fmt:message var="pageTitle" key="page.title.enrolment_by_user_search"/>
		<fmt:message var="breadcrumbTitle" key="breadcrumb.title.user"/>
		<fmt:message var="searchTitle" key="step.search.title.user"/>
		<fmt:message var="sInstruction" key="step.search.instruction.user"/>		
		<fmt:message var="resultDetails" key="step.result.title.enrolment_by_user_search"/>
		<c:set var="formActionUri" value="${form.requestURI}"/>
	</c:otherwise>
</c:choose>
	
<bbNG:genericPage authentication="Y" navItem="admin_main" entitlement="system.plugin.MODIFY">
	<tiles:insertAttribute name="script"/>
    
    <bbNG:cssFile href="${pageContext.request.contextPath}/resources/styles/dsk.css" />
  
	<bbNG:pageHeader>
		<bbNG:pageTitleBar title="${pageTitle}" />
		<bbNG:breadcrumbBar environment="SYS_ADMIN" navItem="admin_main">
			<bbNG:breadcrumb href="${pageContext.request.contextPath}/user">${defaultPageTitle}</bbNG:breadcrumb>
			<bbNG:breadcrumb>${breadcrumbTitle}</bbNG:breadcrumb>
		</bbNG:breadcrumbBar>
	</bbNG:pageHeader>
	    
	<%-- Show error message --%>
	<c:if test="${not empty errorCode}">
		<h3 style="color : red;"><strong><fmt:message key="${errorCode}"/></strong></h3>
	</c:if>
	
	<!-- Action Control Bar -->
	<tiles:insertAttribute name="action_control_bar"/>
	<br/><br/>
	<form:form action="${formActionUri}" modelAttribute="form" method="post">
		<c:if test="${not empty selectAllMode}">
 			<input type="hidden" name="selectAllMode" value="${selectAllMode}"/>
 		</c:if>	
		<h3 id="steptitle1" class="steptitle">${searchTitle}</h3>
		<ol>
			<li class="stepHelp">${sInstruction}</li>
			<li> 
				<tiles:insertAttribute name="search" />
				<tiles:insertAttribute name="search_date" />
			</li>
		</ol>
		<!-- Outcome Area -->
		<c:choose>
			<c:when test="${not empty USERS}">
				<c:set var="showOutcome" value="true"/>
	 			<c:set var="showSubmitButtons" value="true"/>
	 			<c:set var="vInventoryTile" value="user_inventory"/>
	 			<c:set var="vUpdateTile" value="menu_update"/>
	 		</c:when>
	 		<c:when test="${not empty COURSES}">
	 			<c:set var="showOutcome" value="true"/>
	 			<c:set var="showSubmitButtons" value="true"/>
	 			<c:set var="vInventoryTile" value="course_inventory"/>
	 			<c:set var="vUpdateTile" value="menu_update"/>
			</c:when>
			<c:when test="${not empty ORGANISATIONS}">
				<c:set var="showOutcome" value="true"/>
				<c:set var="showSubmitButtons" value="true"/>
	 			<c:set var="vInventoryTile" value="course_inventory"/>
	 			<c:set var="vUpdateTile" value="menu_update"/>
			</c:when>
	 		<c:when test="${not empty COURSE_ENROLMENTS}">
	 			<c:set var="showOutcome" value="true"/>
	 			<c:set var="showSubmitButtons" value="true"/>
	 			<c:set var="vInventoryTile" value="course_enrolment_inventory"/>
	 			<c:set var="vUpdateTile" value="menu_update"/>
	 		</c:when>
	 		<c:when test="${not empty ORGANISATION_ENROLMENTS}">
	 			<c:set var="showOutcome" value="true"/>
	 			<c:set var="showSubmitButtons" value="true"/>
	 			<c:set var="vInventoryTile" value="course_enrolment_inventory"/>
	 			<c:set var="vUpdateTile" value="menu_update"/>
	 		</c:when>
	 		<c:when test="${not empty USER_ENROLMENTS}">
	 			<c:set var="showOutcome" value="true"/>
	 			<c:set var="showSubmitButtons" value="true"/>
	 			<c:set var="vInventoryTile" value="user_enrolment_inventory"/>
	 			<c:set var="vUpdateTile" value="menu_update"/>
	 		</c:when>
	 	</c:choose>
	 	<c:if test="${showOutcome=='true'}">
	 		<bbNG:dataCollection showSubmitButtons="${showSubmitButtons}">
				<bbNG:step id="dskS1" title="${resultDetails}">
 					<bbNG:dataElement>
						<tiles:insertAttribute name="${vInventoryTile}" />
					</bbNG:dataElement>
				</bbNG:step>
				<bbNG:step id="dskS2" title="${tUpdate}" instructions="${updateStepInstruction}">
 					<bbNG:dataElement>
						<tiles:insertAttribute name="${vUpdateTile}" />
					</bbNG:dataElement>
				</bbNG:step>
                <bbNG:step id="dskS3" title="${tChangeComment}" instructions="${changeCommentsStepInstruction}">
                    <bbNG:dataElement>
                        <bbNG:textbox name="${CHANGE_COMMENTS_TEXTAREA_KEY}" format="PLAIN_TEXT"/>
                    </bbNG:dataElement>
                </bbNG:step>
				<bbNG:stepSubmit />
			</bbNG:dataCollection>
	 	</c:if>
	</form:form>
</bbNG:genericPage>
