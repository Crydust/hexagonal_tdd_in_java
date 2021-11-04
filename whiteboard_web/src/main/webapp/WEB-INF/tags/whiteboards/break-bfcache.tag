<%@ tag body-content="empty" %>

<script>
    // Break the bfcache (aka page cache) in safari
    window.addEventListener('pageshow', function (event) {
        if (event.persisted) {
            window.location.reload();
        }
    })
</script>
