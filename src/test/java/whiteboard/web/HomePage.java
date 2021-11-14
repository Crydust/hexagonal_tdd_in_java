package whiteboard.web;

import org.openqa.selenium.WebDriver;

public class HomePage {
    private final WebDriver driver;
    private final Navigation navigation;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        navigation = new Navigation(driver);
    }

    public boolean isCurrent() {
        return driver.getTitle().equals("Home");
    }

    public NewPage gotoNew() {
        return navigation.gotoNew();
    }
}
