package whiteboard.web;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Navigation {
    private final WebDriver driver;

    public Navigation(WebDriver driver) {
        this.driver = driver;
    }

    public HomePage loadHome() {
        driver.get("http://localhost:8080/hexagonal_tdd_in_java/");
        return new HomePage(driver);
    }

    public HomePage gotoHome() {
        driver.findElement(By.linkText("Home")).click();
        return new HomePage(driver);
    }

    public NewPage gotoNew() {
        driver.findElement(By.linkText("New whiteboard")).click();
        return new NewPage(driver);
    }

}
