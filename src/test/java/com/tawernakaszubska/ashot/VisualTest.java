package com.tawernakaszubska.ashot;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static java.time.Duration.*;

public class VisualTest {
    private static WebDriver driver;

    private static final String HOME_PAGE_URL = "https://tawernakaszubska.com";
    private static final By ACCOMMODATION_PAGE_EXCLUDED_PART_BY = By.cssSelector("li[class='flex-active-slide'] > a > img");

    private static AShotHelper aShot;

    @BeforeAll
    public static void setup() {
        aShot = new AShotHelper();
    }

    @BeforeEach
    public void setupWebDriver() {
        driver = new ChromeDriver(new ChromeOptions());
        driver.manage().timeouts().implicitlyWait(ofSeconds(10));
        driver.manage().window().maximize();
    }


    @Test
    public void checkHomePage() {
        driver.get(HOME_PAGE_URL);
        aShot.verify(driver, "HOME_PAGE");
    }

    @Test
    public void checkAccommodationPage() {
        driver.get(HOME_PAGE_URL + "/zakwaterowanie/");
        aShot.verify(driver, "ACCOMMODATION_PAGE", ACCOMMODATION_PAGE_EXCLUDED_PART_BY);
    }

    @Test
    public void checkRestaurantPage() {
        driver.get(HOME_PAGE_URL + "/restauracja/");
        aShot.verify(driver, "RESTAURANT_PAGE");
    }

    @Test
    public void checkGalleryPage() {
        driver.get(HOME_PAGE_URL + "/galeria/");
        aShot.verify(driver, "GALLERY_PAGE");
    }

    @Test
    public void checkAboutUsPage() {
        driver.get(HOME_PAGE_URL + "/o-nas/");
        aShot.verify(driver, "ABOUT_US_PAGE");
    }

    @Test
    public void checkContactPage() {
        driver.get(HOME_PAGE_URL + "/kontakt/");
        aShot.verify(driver, "CONTACT_PAGE");
    }

    @AfterEach
    public void cleanup() {
        driver.quit();
    }

    @AfterAll
    public static void printResults() {
        aShot.printResult();
    }
}