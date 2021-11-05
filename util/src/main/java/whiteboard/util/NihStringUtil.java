package whiteboard.util;

import java.net.URLEncoder;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

public final class NihStringUtil {

    private NihStringUtil() {
        // NOOP
    }

    /**
     * Escapes all characters in a String into html entities.
     * <p>
     * You should probably use commons-text
     * <a href="https://commons.apache.org/proper/commons-text/javadocs/api-release/org/apache/commons/text/StringEscapeUtils.html">
     * StringEscapeUtils</a> instead.
     *
     * @param text
     * @return
     */
    public static String escapeXml(String text) {
        if (text == null) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            final char c = text.charAt(i);
            switch (c) {
                case '&' -> sb.append("&amp;");
                case '<' -> sb.append("&lt;");
                case '>' -> sb.append("&gt;");
                case '"' -> sb.append("&#034;");
                case '\'' -> sb.append("&#039;");
                default -> sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * This method adds an attribute to the querystring of an url.
     * <p>
     * I'm obviously reinventing the wheel here. See
     * <a href="https://stackoverflow.com/questions/26177749/how-can-i-append-a-query-parameter-to-an-existing-url">How
     * can I append a query parameter to an existing URL?</a> for better
     * approaches to this problem</p>
     *
     * @param url
     * @param key
     * @param value
     * @return
     */
    public static String addAttributeToQuery(String url, String key, String value) {
        if (url == null) {
            return null;
        }
        requireNonNull(key, "key is null");
        requireNonNull(value, "value is null");
        final int fragmentStart = url.indexOf('#');
        final int queryStart = url.indexOf('?');
        final String base;
        if (queryStart != -1 && (fragmentStart == -1 || queryStart < fragmentStart)) {
            base = url.substring(0, queryStart);
        } else if (fragmentStart != -1) {
            base = url.substring(0, fragmentStart);
        } else {
            base = url;
        }
        final String query;
        if (queryStart != -1 && fragmentStart == -1) {
            query = url.substring(queryStart);
        } else if (queryStart != -1 && queryStart < fragmentStart) {
            query = url.substring(queryStart, fragmentStart);
        } else {
            query = "";
        }
        final char prefix = query.isEmpty() ? '?' : '&';
        final String fragment;
        if (fragmentStart != -1) {
            fragment = url.substring(fragmentStart);
        } else {
            fragment = "";
        }
        return base + query + prefix + URLEncoder.encode(key, UTF_8) + '=' + URLEncoder.encode(value, UTF_8) + fragment;
    }

}
