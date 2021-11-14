package whiteboard.web;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class NewPage {
    private final WebDriver driver;

    public NewPage(WebDriver driver) {
        this.driver = driver;
    }

    public void create(String name) {
        driver.findElement(By.id("name")).sendKeys(name);
        driver.findElement(By.tagName("button")).click();
    }

    public boolean isCurrent() {
        return driver.getTitle().equals("New whiteboard");
    }
}
