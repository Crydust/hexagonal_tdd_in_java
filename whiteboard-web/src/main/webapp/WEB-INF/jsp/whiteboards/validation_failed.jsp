<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="x" tagdir="/WEB-INF/tags/whiteboards" %>
<jsp:useBean id="errors" scope="request" type="java.util.List<whiteboard.ValidationError>"/>
<jsp:useBean id="name" scope="request" type="java.lang.String"/>
<!DOCTYPE html>
<html>
<head>
    <title>Could not create whiteboard</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <x:break-bfcache/>
</head>
<body>
<h1>Could not create whiteboard</h1>
<x:errors errors="${errors}"/>
<x:form name="${name}"/>

<x:navigation/>
</body>
</html>

