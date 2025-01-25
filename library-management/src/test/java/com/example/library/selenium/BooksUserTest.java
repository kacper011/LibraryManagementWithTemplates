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

import javax.sound.sampled.Line;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BooksUserTest {

    private WebDriver driver;
    private WebDriverWait wait;


    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "D:/chromedriver-win64/chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("http://localhost:8080/login");

        WebElement usernameField = driver.findElement(By.name("username"));
        usernameField.sendKeys("Kacper11");

        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys("kacper11");

        WebElement loginButton = driver.findElement(By.cssSelector(".btn-primary"));
        loginButton.click();

        wait.until(ExpectedConditions.urlContains("/books_user"));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".table")));
    }

    @DisplayName("Get Books For User")
    @Test
    public void testGetBooksForUser() {

        WebElement header = driver.findElement(By.tagName("h1"));
        assertNotNull(header);
        assertTrue(header.getText().contains("List of Books") || header.getText().contains("Search Results"));

        WebElement table = driver.findElement(By.className("table"));
        assertNotNull(table);

        List<WebElement> rows = table.findElements(By.tagName("tr"));
        assertTrue(rows.size() > 1);

    }

    @DisplayName("Search Books By Title")
    @Test
    public void testSearchBooksByTitle() {

        WebElement searchField = driver.findElement(By.name("title"));
        searchField.sendKeys("Test");

        WebElement searchButton = driver.findElement(By.cssSelector("button[type='submit']"));
        searchButton.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("table")));

        WebElement table = driver.findElement(By.className("table"));
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        assertTrue(rows.size() > 1);

        boolean found = rows.stream()
                .skip(1)
                .anyMatch(row -> row.getText().contains("Test"));

        assertTrue(found, "The table does not include books with the title 'Test'");
    }




    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
