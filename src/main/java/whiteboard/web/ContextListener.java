package whiteboard.web;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Configuration.WHITEBOARD_REPO.initialize();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            Configuration.WHITEBOARD_REPO.dispose();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
