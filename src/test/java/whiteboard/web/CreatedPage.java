package whiteboard.web;

import org.openqa.selenium.WebDriver;

public class CreatedPage {
    private final WebDriver driver;

    public CreatedPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isCurrent() {
        return driver.getTitle().equals("Created whiteboard");
    }
}
