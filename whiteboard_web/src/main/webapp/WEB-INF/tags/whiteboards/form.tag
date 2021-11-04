<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ attribute name="name" required="true" type="java.lang.String" %>
<%@ tag body-content="empty" %>

<fmt:setBundle basename="messages" scope="request"/>

<c:url var="action" value="/whiteboards/create"/>
<form action="${fn:escapeXml(action)}" method="post">
    <label for="name">Name</label>
    <input type="text" id="name" name="name" value="${fn:escapeXml(name)}" autofocus="autofocus"/>
    <br/>
    <button type="submit">Save changes</button>
</form>
