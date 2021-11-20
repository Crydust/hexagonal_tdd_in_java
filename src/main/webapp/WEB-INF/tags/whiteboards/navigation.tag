<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="x" tagdir="/WEB-INF/tags/whiteboards" %>

<%@ tag body-content="empty" %>

<p>[
    <x:navigation-link href="/">Home</x:navigation-link>
    |
    <x:navigation-link href="/whiteboards/new">New whiteboard</x:navigation-link>
    |
    <x:navigation-link href="/whiteboards/list">All whiteboards</x:navigation-link>
    ]</p>
