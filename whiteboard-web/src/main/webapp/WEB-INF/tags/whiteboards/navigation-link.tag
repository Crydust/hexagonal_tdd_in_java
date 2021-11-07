<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ attribute name="href" required="true" type="java.lang.String" %>
<%@ tag body-content="scriptless" %>

<c:set var="currentUrl" value="${pageContext.request.getAttribute('jakarta.servlet.forward.request_uri')}"/>
<c:if test="${empty currentUrl}">
    <c:set var="currentUrl" value="${pageContext.request.requestURI}"/>
</c:if>

<c:set var="linkUrl" value="${pageContext.request.contextPath += href}"/>

<c:choose>
    <c:when test="${currentUrl eq linkUrl}">
        <b><jsp:doBody/></b>
    </c:when>
    <c:otherwise>
        <c:url var="url" value="${href}"/>
        <a href="${fn:escapeXml(url)}"><jsp:doBody/></a>
    </c:otherwise>
</c:choose>
