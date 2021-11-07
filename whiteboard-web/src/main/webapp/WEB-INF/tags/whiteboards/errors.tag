<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ attribute name="errors" required="true" type="java.util.List<whiteboard.core.ValidationError>" %>
<%@ tag body-content="empty" %>

<fmt:setBundle basename="messages" scope="request"/>

<c:if test="${not empty errors}">
    <ul>
        <c:forEach var="error" items="${errors}">
            <fmt:message var="message" key="validations.${error.errorCode}">
                <fmt:param value="${error.field}"/>
            </fmt:message>
            <li>${fn:escapeXml(message)}</li>
        </c:forEach>
    </ul>
</c:if>
