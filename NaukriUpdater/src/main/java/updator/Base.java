package updator;

import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.JavascriptExecutor;
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
			String configPath = System.getProperty("user.dir") + "/src/main/resources/config.properties";
			File file = new File(configPath);
			FileInputStream fis = new FileInputStream(file);
			prop = new Properties();
			prop.load(fis);
		} catch (Exception e) {
			e.printStackTrace();
		}
		initialization(prop.getProperty("browser"));

	}

	public void initialization(String browser) {
		if (driver == null) {
			ChromeOptions options = new ChromeOptions();
			if (browser.equalsIgnoreCase("chrome")) {
				options.addArguments("--start-maximized");
				options.addArguments("--incognito");
				options.addArguments("--disable-notifications");

			} else if (browser.equalsIgnoreCase("headless")) {

				options.addArguments("--headless=new");        // new headless mode (Chrome 109+)
				options.addArguments("--window-size=1920,1080");
				options.addArguments("--incognito");
				options.addArguments("--disable-notifications");
				options.addArguments("--disable-gpu");
				options.addArguments("--no-sandbox");
				options.addArguments("--disable-dev-shm-usage");
				options.addArguments("--remote-allow-origins=*"); // for Chrome 111+ compatibility
				options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) " + 
	                     "AppleWebKit/537.36 (KHTML, like Gecko) " +
	                     "Chrome/139.0.7258.155 Safari/537.36");
			}
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver(options);
			driver.get(prop.getProperty("url"));
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60) );

		}
	}

	public void enterValue(String value, WebElement element) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
			   wait.until(webDriver -> ((JavascriptExecutor) webDriver)
					    .executeScript("return document.readyState").equals("complete"));
			wait.until(ExpectedConditions.visibilityOf(element));
			element.clear();
			element.sendKeys(value);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void clickElement(WebElement element) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
			  wait.until(webDriver -> ((JavascriptExecutor) webDriver)
					    .executeScript("return document.readyState").equals("complete"));
			wait.until(ExpectedConditions.elementToBeClickable(element));
			((JavascriptExecutor)driver).executeScript("arguments[0].click();", element);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getText(WebElement element) {
		String text = "";
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
			wait.until(webDriver -> ((JavascriptExecutor) webDriver)
				    .executeScript("return document.readyState").equals("complete"));
		wait.until(ExpectedConditions.elementToBeClickable(element));
			wait.until(ExpectedConditions.elementToBeClickable(element));
			text = element.getText();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;

	}

	public void scrollToElement(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", element);

	}

	public void tearDown() {
		driver.close();
		driver.quit();

	}
}
