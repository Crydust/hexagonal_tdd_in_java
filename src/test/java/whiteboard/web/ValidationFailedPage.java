package whiteboard.web;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ValidationFailedPage {
    private static final By VALIDATION_MESSAGES_LOCATOR = By.cssSelector("h1 + ul > li");
    private final WebDriver driver;
    private final Navigation navigation;

    public ValidationFailedPage(WebDriver driver) {
        this.driver = driver;
        navigation = new Navigation(driver);
    }

    public List<String> getValidationMessages() {
        return driver.findElements(VALIDATION_MESSAGES_LOCATOR).stream()
            .map(WebElement::getText)
            .toList();
    }

    public boolean isCurrent() {
        return driver.getTitle().equals("Could not create whiteboard");
    }

    public NewPage gotoNew() {
        return navigation.gotoNew();
    }

    public HomePage gotoHome() {
        return navigation.gotoHome();
    }
}
