package whiteboard.util.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public final class NihServletUtil {

    private static final String PRG_ATTRIBUTES = "PRG_ATTRIBUTES";

    private NihServletUtil() {
        // NOOP
    }

    private static void noCache(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0L);
    }

    public static void renderText(HttpServletResponse response, String text) {
        noCache(response);
        response.setContentType("text/plain; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().println(text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void renderJsp(HttpServletRequest request, HttpServletResponse response, String jsp) {
        renderJsp(request, response, jsp, Map.of());
    }

    public static void renderJsp(HttpServletRequest request, HttpServletResponse response, String jsp, Map<String, Object> attributes) {
        noCache(response);
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        attributes.forEach(request::setAttribute);
        // readRedirectAttributes(request).forEach(request::setAttribute);
        try {
            request.getRequestDispatcher(jsp).forward(request, response);
        } catch (ServletException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void redirect(HttpServletRequest request, HttpServletResponse response, String url) {
        redirect(request, response, url, Map.of());
    }

    public static void redirect(HttpServletRequest request, HttpServletResponse response, String url, Map<String, ?> requestAttributes) {
        writeRedirectAttributes(request, requestAttributes);
        try {
            response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + url));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Serializable> readRedirectAttributes(HttpServletRequest request) {
        final HttpSession session = request.getSession(false);
        if (session != null
            && session.getAttribute(PRG_ATTRIBUTES) instanceof Map map) {
            session.removeAttribute(PRG_ATTRIBUTES);
            @SuppressWarnings("unchecked") final Map<String, Serializable> castMap = (Map<String, Serializable>) map;
            return castMap;
        }
        return Map.of();
    }

    private static void writeRedirectAttributes(HttpServletRequest request, Map<String, ?> attributes) {
        if (attributes != null && !attributes.isEmpty()) {
            attributes.forEach((key, value) -> {
                if (!(value instanceof Serializable)) {
                    throw new IllegalArgumentException("attribute " + key + " isn't serializable");
                }
            });
            final Serializable value = attributes instanceof Serializable
                ? (Serializable) attributes
                : new HashMap<>(attributes);
            request.getSession(true)
                .setAttribute(PRG_ATTRIBUTES, value);
        }
    }

}
