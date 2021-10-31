package whiteboard.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import whiteboard.CreateWhiteboardObserver;
import whiteboard.UseCases;
import whiteboard.ValidationError;

import java.util.List;
import java.util.Map;

import static whiteboard.boilerplate.NihServletUtil.readRedirectAttributes;
import static whiteboard.boilerplate.NihServletUtil.redirect;
import static whiteboard.boilerplate.NihServletUtil.renderJsp;
import static whiteboard.boilerplate.NihServletUtil.renderText;
import static whiteboard.servlets.Configuration.WHITEBOARD_REPO;

@WebServlet(name = "WhiteboardsServlet", urlPatterns = {
    WhiteboardsServlet.NEW_URL,
    WhiteboardsServlet.CREATE_URL,
    WhiteboardsServlet.CREATED_URL,
    WhiteboardsServlet.VALIDATION_FAILED_URL
})
public class WhiteboardsServlet extends HttpServlet {

    public static final String NEW_URL = "/whiteboards/new";
    public static final String CREATE_URL = "/whiteboards/create";
    public static final String CREATED_URL = "/whiteboards/created";
    public static final String VALIDATION_FAILED_URL = "/whiteboards/validation_failed";

    private static final String NEW_JSP = "/WEB-INF/jsp/whiteboards/new.jsp";
    private static final String VALIDATION_FAILED_JSP = "/WEB-INF/jsp/whiteboards/validation_failed.jsp";
    private static final String CREATED_JSP = "/WEB-INF/jsp/whiteboards/created.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            readRedirectAttributes(request).forEach(request::setAttribute);
            switch (request.getServletPath()) {
                case NEW_URL -> renderJsp(request, response, NEW_JSP);
                case VALIDATION_FAILED_URL -> renderJsp(request, response, VALIDATION_FAILED_JSP);
                case CREATED_URL -> renderJsp(request, response, CREATED_JSP, Map.of(
                    "whiteboard", WHITEBOARD_REPO.findByName(
                        String.valueOf(request.getAttribute("name")))));
                default -> renderText(response, "not implemented");
            }
        } catch (Exception e) {
            e.printStackTrace();
            renderText(response, "error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            if (CREATE_URL.equals(request.getServletPath())) {
                final String name = request.getParameter("name");
                UseCases.createWhiteboard(name, new Gui(request, response, name), WHITEBOARD_REPO);
            } else {
                renderText(response, "not implemented");
            }
        } catch (Exception e) {
            e.printStackTrace();
            renderText(response, "error");
        }
    }

    private record Gui(
        HttpServletRequest request,
        HttpServletResponse response,
        String name
    ) implements CreateWhiteboardObserver {

        @Override
        public void validationFailed(List<ValidationError> errors) {
            redirect(request, response, VALIDATION_FAILED_URL, Map.of("errors", errors, "name", name));
        }

        @Override
        public void whiteboardCreated(Object id) {
            redirect(request, response, CREATED_URL, Map.of(
                "id", String.valueOf(id),
                "name", name
            ));
        }
    }

}
