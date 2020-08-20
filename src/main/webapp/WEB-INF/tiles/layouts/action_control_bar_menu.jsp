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

<c:set var="b2ContextPath" value="${pageContext.request.contextPath}"/>
<fmt:message var="bCourseTitle" key="action.control.bar.button.title.course"/>
<fmt:message var="bCourseUrl" key="action.control.bar.button.url.course"/>
<fmt:message var="bCategoryTitle" key="action.control.bar.button.title.category"/>
<fmt:message var="bCategoryUrl" key="action.control.bar.button.url.category"/>
<fmt:message var="bUserTitle" key="action.control.bar.button.title.user"/>
<fmt:message var="bUserUrl" key="action.control.bar.button.url.user"/>
<fmt:message var="tCourseEnrolment" key="action.control.bar.button.title.course_enrolment"/>
<fmt:message var="tUserEnrolment" key="action.control.bar.button.title.user_enrolment"/>

<fmt:message var="mCourseTitle" key="action.control.bar.menu.title.course"/>
<fmt:message var="iCourseCategoryTitle" key="action.control.bar.menu.course.item.title.category"/>
<fmt:message var="iCourseCategoryHref" key="action.control.bar.menu.course.item.href.category"/>
<fmt:message var="iCourseCategoryTarget" key="action.control.bar.menu.course.item.target.caetgroy"/>
<fmt:message var="iCourseCourseTitle" key="action.control.bar.menu.course.item.title.course"/>
<fmt:message var="iCourseCourseHref" key="action.control.bar.menu.course.item.href.course"/>
<fmt:message var="iCourseCourseTarget" key="action.control.bar.menu.course.item.target.course"/>

<fmt:message var="mOrganisationTitle" key="action.control.bar.menu.title.organisation"/>
<fmt:message var="iOrganisationCategoryTitle" key="action.control.bar.menu.organisation.item.title.category"/>
<fmt:message var="iOrganisationCategoryHref" key="action.control.bar.menu.organisation.item.href.category"/>
<fmt:message var="iOrganisationCategoryTarget" key="action.control.bar.menu.organisation.item.target.caetgroy"/>
<fmt:message var="iOrganisationOrganisationTitle" key="action.control.bar.menu.organisation.item.title.organisation"/>
<fmt:message var="iOrganisationOrganisationHref" key="action.control.bar.menu.organisation.item.href.organisation"/>
<fmt:message var="iOrganisationOrganisationTarget" key="action.control.bar.menu.organisation.item.target.organisation"/>

<fmt:message var="mEnrolmentTitle" key="action.control.bar.menu.title.enrolment"/>
<fmt:message var="iEnrolmentCourseTitle" key="action.control.bar.menu.enrolment.item.title.course"/>
<fmt:message var="iEnrolmentCourseHref" key="action.control.bar.menu.enrolment.item.href.course"/>
<fmt:message var="iEnrolmentCourseTarget" key="action.control.bar.menu.enrolment.item.target.course"/>
<fmt:message var="iEnrolmentOrganisationTitle" key="action.control.bar.menu.enrolment.item.title.organisation"/>
<fmt:message var="iEnrolmentOrganisationHref" key="action.control.bar.menu.enrolment.item.href.organisation"/>
<fmt:message var="iEnrolmentOrganisationTarget" key="action.control.bar.menu.enrolment.item.target.organisation"/>
<fmt:message var="iEnrolmentUserTitle" key="action.control.bar.menu.enrolment.item.title.user"/>
<fmt:message var="iEnrolmentUserHref" key="action.control.bar.menu.enrolment.item.href.user"/>
<fmt:message var="iEnrolmentUserTarget" key="action.control.bar.menu.enrolment.item.target.user"/>

<bbNG:actionControlBar>
	<bbNG:actionButton title="${bUserTitle}" url="${b2ContextPath}${bUserUrl}" primary="true"/>
	<bbNG:actionMenu title="${mCourseTitle}">
    	<bbNG:actionMenuItem title="${iCourseCourseTitle}" href="${b2ContextPath}${iCourseCourseHref}"/>
		<bbNG:actionMenuItem title="${iCourseCategoryTitle}" href="${b2ContextPath}${iCourseCategoryHref}"/>
	</bbNG:actionMenu>
	<bbNG:actionMenu title="${mOrganisationTitle}">
    	<bbNG:actionMenuItem title="${iOrganisationOrganisationTitle}" href="${b2ContextPath}${iOrganisationOrganisationHref}"/>
		<bbNG:actionMenuItem title="${iOrganisationCategoryTitle}" href="${b2ContextPath}${iOrganisationCategoryHref}"/>
	</bbNG:actionMenu>
	<bbNG:actionMenu title="${mEnrolmentTitle}">
		<bbNG:actionMenuItem title="${iEnrolmentCourseTitle}" href="${b2ContextPath}/enrolment/course"/>
		<bbNG:actionMenuItem title="${iEnrolmentOrganisationTitle}" href="${b2ContextPath}/enrolment/organisation"/>
		<bbNG:actionMenuItem title="${iEnrolmentUserTitle}" href="${b2ContextPath}/enrolment/user"/>
	</bbNG:actionMenu>
</bbNG:actionControlBar>
