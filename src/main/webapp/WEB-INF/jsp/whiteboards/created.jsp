<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="x" tagdir="/WEB-INF/tags/whiteboards" %>
<jsp:useBean id="whiteboard" scope="request" type="whiteboard.core.Whiteboard"/>
<!DOCTYPE html>
<html>
<head>
    <title>Created whiteboard</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <x:break-bfcache/>
</head>
<body>
<h1>Created whiteboard</h1>
<dl>
    <dt>Id</dt>
    <dd>${fn:escapeXml(whiteboard.id)}</dd>
    <dt>Name</dt>
    <dd>${fn:escapeXml(whiteboard.name)}</dd>
</dl>

<x:navigation/>
</body>
</html>

