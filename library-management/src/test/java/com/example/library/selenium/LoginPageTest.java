package com.example.library.selenium;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import static org.junit.jupiter.api.Assertions.*;

public class LoginPageTest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "D:/chromedriver-win64/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("http://localhost:8080/login");
    }

    @DisplayName("Login Page Elements")
    @Test
    public void testLoginPageElements() {

        WebElement usernameField = driver.findElement(By.name("username"));
        assertNotNull(usernameField, "The username field was not found!");

        WebElement passwordField = driver.findElement(By.name("password"));
        assertNotNull(passwordField, "The password field was not found!");

        WebElement loginButton = driver.findElement(By.cssSelector(".btn-primary"));
        assertNotNull(loginButton, "The Log in button was not found!");

        WebElement registerLink = driver.findElement(By.linkText("Register"));
        assertNotNull(registerLink, "The link Register was not found!");
    }

    @DisplayName("Successful Login")
    @Test
    public void testSuccessfulLogin() {

        WebElement usernameField = driver.findElement(By.name("username"));
        usernameField.sendKeys("Kacper11");

        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys("kacper11");

        WebElement loginButton = driver.findElement(By.cssSelector(".btn-primary"));
        loginButton.click();

        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("http://localhost:8080/books_user"));
    }
}
