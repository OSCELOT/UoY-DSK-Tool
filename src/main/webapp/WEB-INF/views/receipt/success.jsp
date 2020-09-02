<%@ include file="/WEB-INF/includes/include.jsp" %>
<p><strong>Action is performed successfully.</strong></p>

<c:if test="${not empty usernames}">
    <c:forEach items="${usernames}" var="username">
        <p><c:out value="${username}"/></p>
    </c:forEach>
</c:if>

<c:if test="${not empty SELECTED_COURSES}">
    <c:forEach items="${SELECTED_COURSES}" var="course">
        <p><c:out value="${course}"/></p>
    </c:forEach>
</c:if>

<c:if test="${not empty SELECTED_ENROLLMENTS}">
    <c:forEach items="${SELECTED_ENROLLMENTS}" var="enrollment">
        <p><c:out value="${enrollment}"/></p>
    </c:forEach>
</c:if>

<c:if test="${not empty isUpdateRequired}">
    <p><c:out value="${isUpdateRequired}"/></p>
</c:if>

<c:if test="${not empty selectedCourseRole}">
    <p><c:out value="${selectedCourseRole}"/></p>
</c:if>

<c:if test="${not empty selectedRowStatus}">
    <p><c:out value="${selectedRowStatus}"/></p>
</c:if>

<c:if test="${not empty selectedAvailability}">
    <p><c:out value="${selectedAvailability}"/></p>
</c:if>

<c:if test="${not empty selectedDataSourceKey}">
    <p><c:out value="${selectedDataSourceKey}"/></p>
</c:if>


<!-- User Enrollment -->
<c:if test="${not empty selectedUserName}">
    <p><c:out value="${selectedUserName}"/></p>
</c:if>


   