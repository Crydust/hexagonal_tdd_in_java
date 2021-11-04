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
import java.util.function.Consumer;

import static whiteboard.boilerplate.NihServletUtil.readRedirectAttributes;
import static whiteboard.boilerplate.NihServletUtil.redirect;
import static whiteboard.boilerplate.NihServletUtil.renderJsp;
import static whiteboard.boilerplate.NihServletUtil.renderText;
import static whiteboard.boilerplate.NihStringUtil.addAttributeToQuery;
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
        doGetOrPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        doGetOrPost(request, response);
    }

    private void doGetOrPost(HttpServletRequest request, HttpServletResponse response) {
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
                case VALIDATION_FAILED_URL -> renderJsp(request, response, VALIDATION_FAILED_JSP);
                case CREATED_URL -> renderJsp(request, response, CREATED_JSP, Map.of(
                    "whiteboard", WHITEBOARD_REPO.findById(Long.valueOf(request.getParameter("id")))
                ));
                default -> renderText(response, "not implemented");
            }
        } catch (Exception e) {
            e.printStackTrace();
            renderText(response, "error");
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
