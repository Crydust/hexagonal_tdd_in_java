package whiteboard.web;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ErrorPage {
    private static final By VALIDATION_MESSAGE_LOCATOR = By.cssSelector("h1 + p");
    private final WebDriver driver;

    public ErrorPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getValidationMessage() {
        return driver.findElement(VALIDATION_MESSAGE_LOCATOR).getText();
    }

    public boolean isCurrent() {
        return driver.getTitle().equals("Error");
    }
}
