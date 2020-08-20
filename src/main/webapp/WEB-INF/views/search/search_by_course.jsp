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
<c:choose>
	<c:when test="${title_view_id=='course_course'}">
	  <c:set var="vSearchKey" value="${courseSearchKeys}"/>
	</c:when>
	<c:when test="${title_view_id=='organisation_organisation'}">
		<c:set var="vSearchKey" value="${organisationSearchKeys}"/>
	</c:when>	
	<c:otherwise>
		<c:set var="vSearchKey" value="${courseSearchKeys}"/>
	</c:otherwise>
</c:choose>
<table> 
	<tbody> 
		<tr>
			<td > 
 				<form:select path="searchKey" items="${vSearchKey}" itemLabel="itemLabel" itemValue="itemValue" />
			</td> 
			<td>
				<form:select path="searchOperator" items="${courseSearchOperators}" itemLabel="itemLabel" itemValue="itemValue" />
				<form:input path="searchText"/> 							
				<%@ include file="/WEB-INF/includes/form/go_button.jsp" %>
			</td> 
		</tr> 
	</tbody> 
</table>
