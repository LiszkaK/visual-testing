package com.tawernakaszubska.applitools;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.TestResultsSummary;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static java.time.Duration.*;

public class VisualTest {
    private static String applitoolsApiKey;

    private static BatchInfo batch;
    private static Configuration config;
    private static ClassicRunner runner;

    private WebDriver driver;
    private Eyes eyes;

    private static final String HOME_PAGE_URL = "https://tawernakaszubska.com";

    @BeforeAll
    public static void setup() {
        applitoolsApiKey = System.getenv("APPLITOOLS_API_KEY");

        runner = new ClassicRunner();

        batch = new BatchInfo("Tawerna Kaszubska");

        config = new Configuration();

        config.setApiKey(applitoolsApiKey);

        config.setBatch(batch);
    }

    @BeforeEach
    public void openBrowserAndEyes(TestInfo testInfo) {
        driver = new ChromeDriver(new ChromeOptions());

        driver.manage().timeouts().implicitlyWait(ofSeconds(10));

        eyes = new Eyes(runner);
        eyes.setConfiguration(config);

        eyes.open(
                driver,
                "Tawerna Kaszubska Web App",
                testInfo.getDisplayName(),
                new RectangleSize(1024, 768));
    }

    @Test
    public void checkHomePage() {
        driver.get(HOME_PAGE_URL);
        eyes.check(Target.window().fully().withName("Tawerna Kaszubska"));
    }

    @Test
    public void checkAccommodationPage() {
        driver.get(HOME_PAGE_URL + "/zakwaterowanie/");
        eyes.check(Target.window().fully().withName("Zakwaterowanie | Tawerna Kaszubska"));
    }

    @Test
    public void checkRestaurantPage() {
        driver.get(HOME_PAGE_URL + "/restauracja/");
        eyes.check(Target.window().fully().withName("Restauracja | Tawerna Kaszubska"));
    }

    @Test
    public void checkGalleryPage() {
        driver.get(HOME_PAGE_URL + "/galeria/");
        eyes.check(Target.window().fully().withName("Galeria | Tawerna Kaszubska"));
    }

    @Test
    public void checkAboutUsPage() {
        driver.get(HOME_PAGE_URL + "/o-nas/");
        eyes.check(Target.window().fully().withName("O nas | Tawerna Kaszubska"));
    }

    @Test
    public void checkContactPage() {
        driver.get(HOME_PAGE_URL + "/kontakt/");
        eyes.check(Target.window().fully().withName("Kontakt | Tawerna Kaszubska"));
    }

    @AfterEach
    public void cleanup() {
        driver.quit();
        eyes.closeAsync();
    }

    @AfterAll
    public static void printResults() {
        TestResultsSummary allTestResults = runner.getAllTestResults();
        System.out.println(allTestResults);
    }
}