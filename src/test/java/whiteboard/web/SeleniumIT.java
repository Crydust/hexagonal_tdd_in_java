package whiteboard.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;

class SeleniumIT {
    WebDriver driver;

    @BeforeEach
    void setUp() {
        driver = new HtmlUnitDriver();
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    void shouldCreateWhiteboard() {
        new Navigation(driver)
            .loadHome()
            .gotoNew()
            .create(UUID.randomUUID().toString());
        assertThat(new CreatedPage(driver).isCurrent(), is(true));
    }

    @Test
    void shouldValidateRequiredField() {
        new Navigation(driver)
            .loadHome()
            .gotoNew()
            .create("");
        final ValidationFailedPage validationFailedPage = new ValidationFailedPage(driver);
        assertAll(
            () -> assertThat(validationFailedPage.isCurrent(), is(true)),
            () -> assertThat(validationFailedPage.getValidationMessages(), contains("name must be provided"))
        );
    }

    @Test
    void shouldNotCacheValidationErrors() {
        new Navigation(driver)
            .loadHome()
            .gotoNew()
            .create("");
        final ValidationFailedPage validationFailedPage = new ValidationFailedPage(driver);
        assertThat(validationFailedPage.isCurrent(), is(true));
        driver.navigate().refresh();
        final ErrorPage errorPage = new ErrorPage(driver);
        assertAll(
            () -> assertThat(errorPage.isCurrent(), is(true)),
            () -> assertThat(errorPage.getValidationMessage(), is("This page has expired."))
        );
    }

    @Test
    void shouldNotCacheValidationErrorsInBFCache() {
        new Navigation(driver)
            .loadHome()
            .gotoNew()
            .create("");
        final ValidationFailedPage validationFailedPage = new ValidationFailedPage(driver);
        assertThat(validationFailedPage.isCurrent(), is(true));
        final HomePage homePage = validationFailedPage.gotoHome();
        assertThat(homePage.isCurrent(), is(true));
        driver.navigate().back();
        final ErrorPage errorPage = new ErrorPage(driver);
        assertAll(
            () -> assertThat(errorPage.isCurrent(), is(true)),
            () -> assertThat(errorPage.getValidationMessage(), is("This page has expired."))
        );
    }

}
