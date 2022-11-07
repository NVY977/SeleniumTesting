package ru.nvy.tpo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class KinoPoiskTest {
    private static WebDriver driver;
    private static final String PATH = "src/drivers/chromedriver.exe";

    private static final String URL = "https://www.kinopoisk.ru/";

    @BeforeClass
    public static void setup() {
        System.setProperty("webdriver.chrome.driver", PATH);
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
    }

    @Test
    public void login() throws InterruptedException {
        driver.get(URL);
        WebElement loginButton = driver.findElement(By.xpath("//*[@id=\"__next\"]/div[2]/div[1]/header/div/div[3]/div/button"));
        loginButton.click();
        driver.get("https://passport.yandex.ru/auth?origin=kinopoisk&retpath=https%3A%2F%2Fsso.passport.yandex.ru%2Fpush%3Fretpath%3Dhttps%253A%252F%252Fwww.kinopoisk.ru%252Fapi%252Fprofile-pending%252F%253Fretpath%253Dhttps%25253A%25252F%25252Fwww.kinopoisk.ru%25252F%26uuid%3Db8de0f11-0473-4473-bb50-d1c44a444c9d");
        WebElement emailLoginButton = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div[2]/div/div/div[2]/div[3]/div/div/div/div[1]/form/div[1]/div[1]/button"));
        emailLoginButton.click();
        WebElement loginField = driver.findElement(By.xpath("//*[@id=\"passp-field-login\"]"));
        Thread.sleep(200);
        loginField.sendKeys("[your_login]");
        loginButton = driver.findElement(By.xpath("//*[@id=\"passp:sign-in\"]"));
        loginButton.click();
        WebElement passwordField = driver.findElement(By.xpath("//*[@id=\"passp-field-passwd\"]"));
        Thread.sleep(200);
        passwordField.sendKeys("[your_password]");
        loginButton = driver.findElement(By.xpath("//*[@id=\"passp:sign-in\"]"));
        loginButton.click();

        Thread.sleep(1500);
        driver.get("[your_link_profile]");
        WebElement login = driver.findElement(By.xpath("//*[@id=\"profileInfoWrap\"]/div[2]/div[1]/div[3]/a"));
        Assert.assertEquals(login.getText(), "Изменить данные");
    }

    @Test
    public void searchFilmPositive() {
        driver.get(URL);
        WebElement searchField = driver.findElement(By.xpath("//*[@id=\"__next\"]/div[2]/div[1]/header/div/div[2]/div[2]/div/form/div/input"));
        String searchString = "Вышка";
        searchField.sendKeys(searchString);
        WebElement searchButton = driver.findElement(By.xpath("//*[@id=\"__next\"]/div[2]/div[1]/header/div/div[2]/div[2]/div/form/div/div/button"));
        searchButton.click();
        driver.get("https://www.kinopoisk.ru/index.php?kp_query=" + searchString);
        WebElement found = driver.findElement(By.xpath("//*[@id=\"block_left_pad\"]/div/div[2]/div/div[2]/p/a"));
        Assert.assertTrue(found.getText().contains("Вышка"));

    }

    @Test
    public void searchFilmNegative() {
        driver.get(URL);
        WebElement searchField = driver.findElement(By.xpath("//*[@id=\"__next\"]/div[2]/div[1]/header/div/div[2]/div[2]/div/form/div/input"));
        String searchString = "аыфрвоалфыво";
        searchField.sendKeys(searchString);
        WebElement searchButton = driver.findElement(By.xpath("//*[@id=\"__next\"]/div[2]/div[1]/header/div/div[2]/div[2]/div/form/div/div/button"));
        searchButton.click();
        driver.get("https://www.kinopoisk.ru/index.php?kp_query=" + searchString);
        WebElement found = driver.findElement(By.xpath("//*[@id=\"block_left_pad\"]/div/table/tbody/tr[1]/td/h2"));
        Assert.assertTrue(found.getText().contains("К сожалению, по вашему запросу ничего не найдено..."));
    }

    @AfterClass
    public static void logOut() {
        driver.quit();
    }

}
