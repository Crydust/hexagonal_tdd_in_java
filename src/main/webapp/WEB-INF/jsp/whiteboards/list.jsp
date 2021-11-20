<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="x" tagdir="/WEB-INF/tags/whiteboards" %>
<jsp:useBean id="whiteboards" scope="request" type="java.util.List<whiteboard.core.Whiteboard>"/>
<!DOCTYPE html>
<html>
<head>
    <title>All whiteboards</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <x:break-bfcache/>
</head>
<body>
<h1>All whiteboards</h1>
<c:forEach var="whiteboard" items="${whiteboards}">
    <x:view whiteboard="${whiteboard}"/>
</c:forEach>

<x:navigation/>
</body>
</html>

