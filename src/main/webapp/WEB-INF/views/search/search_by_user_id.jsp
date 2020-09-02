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
<table> 
	<tbody> 
		<tr> 
			<td align="right"><fmt:message key="search.user.label.search_by_id"/> </td>
			<td>
				<form:input path="searchText"/>
				<%@ include file="/WEB-INF/includes/form/go_button.jsp" %> 							
			</td> 
		</tr> 
<!-- 		<tr>
			<td align="right"><fmt:message key="search.user.label.select_category"/> </td>
			<td>
				<form:select path="category">
					<form:option value="-1" label="Select ..." />
					<form:options items="${categories}"  itemLabel="itemLabel" itemValue="itemValue" />
				</form:select> 
			</td>
		</tr>
		<tr>
			<td align="right"><fmt:message key="search.user.label.select_organisation_category"/> </td>
			<td>
				<form:select path="organisationCategory">
					<form:option value="-1" label="Select ..." />
					<form:options items="${organisationCategories}"  itemLabel="itemLabel" itemValue="itemValue" />
				</form:select> 
			</td>
		</tr> -->	
	</tbody> 
</table>
