<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="x" tagdir="/WEB-INF/tags/whiteboards" %>
<jsp:useBean id="errors" scope="request" type="java.util.List<whiteboard.ValidationError>"/>
<jsp:useBean id="name" scope="request" type="java.lang.String"/>
<!DOCTYPE html>
<html>
<head>
    <title>/whiteboards/validation_failed</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<h1>/whiteboards/validation_failed</h1>

<x:errors errors="${errors}"/>

<x:form name="${name}"/>

<c:url var="url" value="/whiteboards/new"/>
<p><a href="${fn:escapeXml(url)}">/whiteboards/new</a></p>
</body>
</html>

