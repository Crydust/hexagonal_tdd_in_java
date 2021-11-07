package whiteboard.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import whiteboard.CreateWhiteboardObserver;
import whiteboard.UseCases;
import whiteboard.ValidationError;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static java.util.Collections.list;
import static whiteboard.servlets.Configuration.WHITEBOARD_REPO;
import static whiteboard.util.web.NihServletUtil.readRedirectAttributes;
import static whiteboard.util.web.NihServletUtil.redirect;
import static whiteboard.util.web.NihServletUtil.renderJsp;
import static whiteboard.util.web.NihStringUtil.addAttributeToQuery;

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
    private static final String EXPIRED_JSP = "/WEB-INF/jsp/expired.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        doGetOrPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        doGetOrPost(request, response);
    }

    private void doGetOrPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            readRedirectAttributes(request).forEach(request::setAttribute);
            switch (request.getServletPath()) {
                case NEW_URL -> renderJsp(request, response, NEW_JSP);
                case CREATE_URL -> {
                    final String name = request.getParameter("name");
                    final Gui gui = new Gui();
                    UseCases.createWhiteboard(name, gui, WHITEBOARD_REPO);
                    gui.then(
                        id -> redirect(request, response, addAttributeToQuery(CREATED_URL, "id", String.valueOf(id))),
                        errors -> redirect(request, response, VALIDATION_FAILED_URL, Map.of("errors", errors, "name", name))
                    );
                }
                case VALIDATION_FAILED_URL -> {
                    if (list(request.getAttributeNames()).containsAll(List.of("errors", "name"))) {
                        renderJsp(request, response, VALIDATION_FAILED_JSP);
                    } else {
                        renderJsp(request, response, EXPIRED_JSP);
                    }
                }
                case CREATED_URL -> renderJsp(request, response, CREATED_JSP, Map.of(
                    "whiteboard", WHITEBOARD_REPO.findById(Long.valueOf(request.getParameter("id")))
                ));
                default -> throw new ServletException("Unknown ServletPath '" + request.getServletPath() + "'");
            }
        } catch (ServletException e) {
            throw e;
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private static final class Gui implements CreateWhiteboardObserver {
        private Result<Long, List<ValidationError>> result = Result.fail(List.of());

        @Override
        public void validationFailed(List<ValidationError> errors) {
            result = Result.fail(errors);
        }

        @Override
        public void whiteboardCreated(Long id) {
            result = Result.success(id);
        }

        public void then(Consumer<Long> successConsumer, Consumer<List<ValidationError>> failureConsumer) {
            result.then(successConsumer, failureConsumer);
        }
    }

}