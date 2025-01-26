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

import static org.junit.jupiter.api.Assertions.*;

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

    @DisplayName("Rent Button Is Disabled For Unavailable Books")
    @Test
    public void testRentButtonIsDisabledForUnavailableBooks() {

        List<WebElement> rentButtons = driver.findElements(By.cssSelector(".btn-primary"));
        assertFalse(rentButtons.isEmpty());

        for (WebElement button : rentButtons) {
            String classAttribute = button.getAttribute("class");
            if (classAttribute.contains("btn-disabled")) {
                assertTrue(classAttribute.contains("btn-disabled"), "The ‘Rent’ button for an unavailable book should be disabled.");
            }
        }
    }

    @DisplayName("View Book Details")
    @Test
    public void testViewBookDetails() {

        driver.get("http://localhost:8080/books/1/view");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("card-header")));

        WebElement header = driver.findElement(By.className("card-header"));
        assertEquals("Book Details", header.getText().trim(), "The heading should be ‘Book Details");

        WebElement titleElement = driver.findElement(By.xpath("//p[contains(text(), 'Book Title:')]/strong"));
        assertNotNull(titleElement);
        assertFalse(titleElement.getText().isEmpty(), "The title of the book should not be empty");

        WebElement authorElement = driver.findElement(By.xpath("//p[contains(text(), 'Book Author:')]/strong"));
        assertNotNull(authorElement);
        assertFalse(authorElement.getText().isEmpty(), "The author of the book should not be empty");

        WebElement availabilityElement = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//p[contains(text(), 'Book Available:')]/strong")
                ));

        String availabilityText = availabilityElement.getText().toLowerCase();

        assertTrue(
                availabilityText.equals("available") || availabilityText.equals("rented"),
                "Availability should have been ‘available’ or ‘rented’, but it was: " + availabilityText
        );

        WebElement summaryElement = driver.findElement(By.xpath("//div[contains(@class, 'book-summary')]/p"));
        assertNotNull(summaryElement);
        assertFalse(summaryElement.getText().isEmpty(), "The summary of the book should not be empty");

    }

    @DisplayName("Rent Book")
    @Test
    public void testRentBook() {

        WebElement rentButton = driver.findElement(By.xpath("//a[@href='/books/4/rent' and text()='Rent']"));

        assertTrue(rentButton.isEnabled(), "Rent button should be enabled for available book");
        rentButton.click();

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlContains("/my_books"));

        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/my_books"), "The user was not redirected to the ‘my_books’ page");
    }



    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
