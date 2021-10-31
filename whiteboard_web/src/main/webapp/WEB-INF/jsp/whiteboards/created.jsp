<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="x" tagdir="/WEB-INF/tags/whiteboards" %>
<jsp:useBean id="whiteboard" scope="request" type="whiteboard.Whiteboard"/>
<!DOCTYPE html>
<html>
<head>
    <title>/whiteboards/created</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<h1>/whiteboards/created</h1>
<dl>
    <dt>Id</dt>
    <dd>${fn:escapeXml(whiteboard.id)}</dd>
    <dt>Name</dt>
    <dd>${fn:escapeXml(whiteboard.name)}</dd>
</dl>

<c:url var="url" value="/whiteboards/new"/>
<p><a href="${fn:escapeXml(url)}">/whiteboards/new</a></p>
</body>
</html>

