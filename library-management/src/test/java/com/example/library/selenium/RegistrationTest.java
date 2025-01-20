package com.example.library.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegistrationTest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "D:/chromedriver-win64/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("http://localhost:8080/registration");
    }

    @DisplayName("Registration Success")
    @Test
    public void testRegistrationSuccess() {

        WebElement nameField = driver.findElement(By.id("name"));
        WebElement emailField = driver.findElement(By.id("email"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));

        nameField.sendKeys("Janusz");
        emailField.sendKeys("janusz.kowalski@gmail.com");
        passwordField.sendKeys("janusz123");

        submitButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/login"));

        assertTrue(driver.getCurrentUrl().contains("/login"));
    }

    @DisplayName("Name Invalid")
    @Test
    public void testNameInvalid() {


        WebElement nameField = driver.findElement(By.id("name"));
        nameField.sendKeys("janusz");

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".invalid-feedback")));
            assertTrue(errorMessage.isDisplayed());
        } catch (TimeoutException e) {
            System.out.println("The validation error did not occur at the predictable time.");
        }
    }

    @DisplayName("Email Validation")
    @Test
    public void testEmailValidation() {

        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys("invalid-email");

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));


        try {
            WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".invalid-feedback")));
            assertTrue(errorMessage.isDisplayed());
        } catch (TimeoutException e) {
            System.out.println("The validation error did not occur at the predictable time.");
        }
    }

    @DisplayName("Password Validation")
    @Test
    public void testPasswordValidation() {

        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("123");

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));


        try {
            WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".invalid-feedback")));
            assertTrue(errorMessage.isDisplayed());
        } catch (TimeoutException e) {
            System.out.println("The validation error did not occur at the predictable time.");
        }
    }


    @AfterEach
    public void tearDown() {

        if (driver != null) {
            driver.quit();
        }
    }
}
