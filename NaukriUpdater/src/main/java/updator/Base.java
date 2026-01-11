package updator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Base {

    WebDriver driver = null;
    Properties prop;

    public Base() {
        try {
            if (isGitHubRun()) {
                prop = loadFromGitHubSecrets();
            } else {
                prop = loadFromPropertiesFile();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load configuration", e);
        }

        initialization(prop.getProperty("browser"));
    }

    private boolean isGitHubRun() {
        return "true".equalsIgnoreCase(System.getenv("GITHUB_ACTIONS"));
    }

    private Properties loadFromPropertiesFile() throws Exception {
        Properties properties = new Properties();
        String configPath = System.getProperty("user.dir")
                + "/src/main/resources/config.properties";

        try (FileInputStream fis = new FileInputStream(new File(configPath))) {
            properties.load(fis);
        }

        return properties;
    }

    private Properties loadFromGitHubSecrets() {
        Properties properties = new Properties();

        properties.setProperty("browser", System.getenv("BROWSER"));
        properties.setProperty("url", System.getenv("URL"));
        properties.setProperty("username", System.getenv("APP_USERNAME"));
        properties.setProperty("password", System.getenv("APP_PASSWORD"));

        return properties;
    }

    public void initialization(String browser) {
        if (driver == null) {
            ChromeOptions options = new ChromeOptions();

            if (browser.equalsIgnoreCase("chrome")) {
                options.addArguments("--start-maximized");
                options.addArguments("--incognito");
                options.addArguments("--disable-notifications");

            } else if (browser.equalsIgnoreCase("headless")) {
                options.addArguments("--headless=new"); 
                options.addArguments("--window-size=1920,1080");
                options.addArguments("--incognito");
                options.addArguments("--disable-notifications");
                options.addArguments("--disable-gpu");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
            }

            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver(options);
            driver.get(prop.getProperty("url"));
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
        }
    }

    public void enterValue(String value, WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));
        wait.until(webDriver ->
                ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState")
                        .equals("complete"));
        wait.until(ExpectedConditions.visibilityOf(element));
        element.clear();
        takeScreenshot(driver);
        element.sendKeys(value);
    }

    public void clickElement(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));
        wait.until(webDriver ->
                ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState")
                        .equals("complete"));
        wait.until(ExpectedConditions.elementToBeClickable(element));
        takeScreenshot(driver);
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", element);
    }

    public String getText(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));
        wait.until(webDriver ->
                ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState")
                        .equals("complete"));
        wait.until(ExpectedConditions.elementToBeClickable(element));
        takeScreenshot(driver);
        return element.getText();
    }

    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public static String takeScreenshot(WebDriver driver) {
        String filename = "screenShot_" + System.currentTimeMillis() + ".png";
        File folder = new File("screenshots");
        if (!folder.exists()) folder.mkdirs();

        File destFile = new File(folder, filename);
        try {
            File srcFile = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.FILE);
            Files.copy(srcFile.toPath(), destFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return destFile.getAbsolutePath();
    }

    public void tearDown() {
        driver.close();
        driver.quit();
    }
}
