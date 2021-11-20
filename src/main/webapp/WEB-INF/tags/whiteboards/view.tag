<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ attribute name="whiteboard" required="true" type="whiteboard.core.Whiteboard" %>
<%@ tag body-content="empty" %>

<dl>
    <dt>Id</dt>
    <dd>${fn:escapeXml(whiteboard.id)}</dd>
    <dt>Name</dt>
    <dd>${fn:escapeXml(whiteboard.name)}</dd>
</dl>
