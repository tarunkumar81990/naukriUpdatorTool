package updator;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Updator extends Base {

	@FindBy(xpath = "//a[@id='login_Layer']")
	WebElement btn_mainLogin;

	@FindBy(xpath = "//label[text()='Email ID / Username']/following-sibling::input")
	WebElement inpt_userName;

	@FindBy(xpath = "//label[text()='Password']/following-sibling::input")
	WebElement inpt_password;

	@FindBy(xpath = "//button[text()='Login']")
	WebElement btn_loginSubmit;

	@FindBy(xpath = "//span[@class='fullname']")
	WebElement txt_existingName;

	@FindBy(xpath = "//img[@alt='naukri user profile img']")
	WebElement link_profileImage;

	@FindBy(xpath = "//a[text()='View & Update Profile']")
	WebElement link_updateProfile;

	@FindBy(xpath = "//em[text()='editOneTheme']")
	WebElement icon_edit;

	@FindBy(xpath = "//span[text()='Name']/following-sibling::input")
	WebElement inpt_profileName;

	@FindBy(xpath = "//button[text()='Save']")
	WebElement btn_save;

	public Updator() {
		super();
		PageFactory.initElements(driver, this);

	}

	public void loginToNaukri() {

		String userName = prop.getProperty("username");
		String password = prop.getProperty("password");
		if (userName == null || userName.isEmpty()) {
			userName = System.getenv("USERNAME"); // mapped from APP_USERNAME
		}
		if (password == null || password.isEmpty()) {
			password = System.getenv("PASSWORD"); // mapped from APP_PASSWORD
		}
		clickElement(btn_mainLogin);
		enterValue(userName, inpt_userName);
		enterValue(password, inpt_password);

		clickElement(btn_loginSubmit);

	}

	public void updateName() throws InterruptedException {
		clickElement(link_profileImage);
		clickElement(link_updateProfile);
		String existingName = getText(txt_existingName);
		String newName = existingName + " ";
		clickElement(icon_edit);
		enterValue(newName, inpt_profileName);
		scrollToElement(btn_save);
		clickElement(btn_save);
		Thread.sleep(5000);
		clickElement(icon_edit);
		enterValue(existingName, inpt_profileName);
		scrollToElement(btn_save);
		clickElement(btn_save);
		System.out.println("Naukri updated successfully");

	}

}
