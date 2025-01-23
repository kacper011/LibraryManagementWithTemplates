package com.example.library.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BooksAdminTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "D:/chromedriver-win64/chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("http://localhost:8080/login");

        WebElement usernameField = driver.findElement(By.name("username"));
        usernameField.sendKeys("admin");

        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys("adminPassword");

        WebElement loginButton = driver.findElement(By.cssSelector(".btn-primary"));
        loginButton.click();

        wait.until(ExpectedConditions.urlContains("/books_admin"));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".table")));
    }

    private void handleAlert() {
        try {
            Alert alert = driver.switchTo().alert();
            alert.accept();
        } catch (NoAlertPresentException e) {
        }
    }


    @DisplayName("Books Admin Page Load")
    @Test
    public void testBooksAdminLoadPage() {

        wait.until(ExpectedConditions.urlToBe("http://localhost:8080/books_admin"));

        assertEquals("http://localhost:8080/books_admin", driver.getCurrentUrl(), "URL is incorrect!");

        WebElement pageTitle = driver.findElement(By.tagName("h1"));
        assertEquals("List of Books", pageTitle.getText());

        WebElement table = driver.findElement(By.cssSelector(".table"));
        assertTrue(table.isDisplayed());

        List<WebElement> rows = table.findElements(By.tagName("tr"));
        assertTrue(rows.size() > 1, "No books in the table.");
    }

    @DisplayName("Admin Actions Visible")
    @Test
    public void testAdminActionsVisible() {

        WebElement editButton = driver.findElement(By.cssSelector("a.btn-primary"));
        WebElement deleteButton = driver.findElement(By.cssSelector("a.btn-danger"));

        assertTrue(editButton.isDisplayed());
        assertTrue(deleteButton.isDisplayed());
    }

    @DisplayName("Logout")
    @Test
    public void testLogout() {

        WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("a[href='/logout']")
        ));

        logoutButton.click();

        wait.until(ExpectedConditions.urlContains("/login"));
        assertTrue(driver.getCurrentUrl().contains("/login"));
    }

    @DisplayName("Access Without Admin Role")
    @Test
    public void testAccessWithoutAdminRole() {

        driver.get("http://localhost:8080/logout");
        wait.until(ExpectedConditions.urlContains("/login"));

        WebElement usernameField = driver.findElement(By.name("username"));
        usernameField.sendKeys("Kacper11");

        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys("kacper11");

        WebElement loginButton = driver.findElement(By.cssSelector(".btn-primary"));
        loginButton.click();

        wait.until(ExpectedConditions.urlContains("/books_user"));
        assertTrue(driver.getCurrentUrl().contains("/books_user"), "Użytkownik z rolą USER powinien zostać przekierowany na /books_user po zalogowaniu");

        driver.get("http://localhost:8080/books_user");
        
        wait.until(ExpectedConditions.urlContains("/books_user"));
        assertTrue(driver.getCurrentUrl().contains("/books_user"), "Użytkownik z rolą USER powinien zostać przekierowany z /books_admin na /books_user");
    }





    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}


