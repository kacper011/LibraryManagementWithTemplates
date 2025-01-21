package com.example.library.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BooksAdminTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "D:/chromedriver-win64/chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Inicjalizowanie WebDriverWait
    }

    @DisplayName("Books Admin Page Load")
    @Test
    public void testBooksAdminLoadPage() {

        driver.get("http://localhost:8080/login");

        WebElement usernameField = driver.findElement(By.name("username"));
        usernameField.sendKeys("admin");

        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys("adminPassword");

        WebElement loginButton = driver.findElement(By.cssSelector(".btn-primary"));
        loginButton.click();

        wait.until(ExpectedConditions.urlToBe("http://localhost:8080/books_admin"));

        assertEquals("http://localhost:8080/books_admin", driver.getCurrentUrl(), "URL is incorrect!");

        WebElement pageTitle = driver.findElement(By.tagName("h1"));
        assertEquals("List of Books", pageTitle.getText());

        WebElement table = driver.findElement(By.cssSelector(".table"));
        assertTrue(table.isDisplayed());
        
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        assertTrue(rows.size() > 1, "No books in the table.");
    }


    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}


