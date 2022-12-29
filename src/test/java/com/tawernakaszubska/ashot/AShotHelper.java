package com.tawernakaszubska.ashot;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AShotHelper {
    private static final String BASE_SCREENSHOT_PATH = "src/main/resources/screenshots/base/";
    private static final String ACTUAL_SCREENSHOT_PATH = "src/main/resources/screenshots/actual/";
    private static final String DIFF_SCREENSHOT_PATH = "src/main/resources/screenshots/diff/";

    private final ArrayList<String> resultMessages = new ArrayList<>();
    private boolean anyTestFailed = false;

    public void verify(WebDriver driver, String pageName) {
        if (!haveBaseLine(pageName)) {
            saveScreenshot(driver, pageName, "BASE");
            resultMessages.add("Empty base page: " + pageName + ". Creating new base page. Run one more time to get diff\n");
            return;
        } else saveScreenshot(driver, pageName, "ACTUAL");

        if (haveDiff(pageName)) {
            saveScreenshot(driver, pageName, "DIFF");
            anyTestFailed = true;
            resultMessages.add("Found different for page: " + pageName + " . Check out diff directory for more details: " + DIFF_SCREENSHOT_PATH + "\n");
        } else resultMessages.add("Test passed. There are no different for page: " + pageName + "\n");
    }

    public void verify(WebDriver driver, String pageName, By excludeElement) {
        if (!haveBaseLine(pageName)) {
            saveScreenshot(driver, pageName, "BASE", excludeElement);
            resultMessages.add("Empty base page: " + pageName + ". Creating new base page. Run one more time to get diff\n");
            return;
        } else saveScreenshot(driver, pageName, "ACTUAL", excludeElement);

        if (haveDiff(pageName)) {
            saveScreenshot(driver, pageName, "DIFF");
            anyTestFailed = true;
            resultMessages.add("Found different for page: " + pageName + " . Check out diff directory for more details: " + DIFF_SCREENSHOT_PATH + "\n");
        } else resultMessages.add("Test passed. There are no different for page: " + pageName + "\n");
    }

    public void printResult() {
        if (!resultMessages.isEmpty()) System.out.println(resultMessages);
        else System.out.println("Result message is empty");
        if (anyTestFailed)
            throw new AssertionError("There are diffs in screenshots. Review test result to get more details");
    }

    private void saveScreenshot(WebDriver driver, String pageName, String screenshotType) {
        String filePath = getFilePath(pageName, screenshotType);

        if (screenshotType.equals("DIFF")) {
            saveDiff(pageName, filePath);
        } else saveScreenShot(driver, filePath);
    }

    private void saveScreenshot(WebDriver driver, String pageName, String screenshotType, By excludeElement) {
        String filePath = getFilePath(pageName, screenshotType);

        if (screenshotType.equals("DIFF")) {
            saveDiff(pageName, filePath);
        } else saveScreenShot(driver, filePath, excludeElement);
    }

    private Screenshot getBaseScreenshot(String pageName) throws IOException {
        return new Screenshot(ImageIO.read(new File(BASE_SCREENSHOT_PATH + pageName + ".png")));
    }

    private Screenshot getActualScreenshot(String pageName) throws IOException {
        return new Screenshot(ImageIO.read(new File(ACTUAL_SCREENSHOT_PATH + pageName + ".png")));
    }

    private boolean haveBaseLine(String pageName) {
        return new File(BASE_SCREENSHOT_PATH + pageName + ".png").isFile();
    }

    private boolean haveDiff(String pageName) {
        ImageDiff diff = null;
        try {
            diff = new ImageDiffer().makeDiff(getBaseScreenshot(pageName), getActualScreenshot(pageName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int size = diff.getDiffSize();
        return size != 0;
    }

    private void saveDiff(String pageName, String diffPath) {
        ImageDiff diff = null;
        try {
            diff = new ImageDiffer().makeDiff(getBaseScreenshot(pageName), getActualScreenshot(pageName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File diffFile = new File(diffPath);
        try {
            ImageIO.write(diff.getMarkedImage(), "png", diffFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveScreenShot(WebDriver driver, String filePath) {
        Screenshot screenshot = new AShot()
                .shootingStrategy(ShootingStrategies.viewportPasting(100))
                .takeScreenshot(driver);

        saveImageFile(filePath, screenshot);
    }

    private void saveScreenShot(WebDriver driver, String filePath, By excludeElement) {
        Screenshot screenshot = new AShot()
                .shootingStrategy(ShootingStrategies.viewportPasting(100))
                .addIgnoredElement(excludeElement)
                .takeScreenshot(driver);

        saveImageFile(filePath, screenshot);
    }

    private String getFilePath(String pageName, String screenshotType) {
        return switch (screenshotType) {
            case "BASE" -> BASE_SCREENSHOT_PATH + pageName + ".png";
            case "ACTUAL" -> ACTUAL_SCREENSHOT_PATH + pageName + ".png";
            case "DIFF" -> DIFF_SCREENSHOT_PATH + pageName + ".png";
            default -> throw new IllegalArgumentException("Illegal Argument Exception. Use: BASE, ACTUAL or DIFF");
        };
    }

    private void saveImageFile(String filePath, Screenshot screenshot) {
        File file = new File(filePath);
        try {
            ImageIO.write(screenshot.getImage(), "png", file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
