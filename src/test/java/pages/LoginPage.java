package pages;

import org.openqa.selenium.By;

public class LoginPage extends WebDriverController{

	//LOGIN FORM
	private static final By FIELD_LOGIN = By.name("username");
	private static final By FIELD_PASSWORD = By.name("password");
	
	 public void login(String username, String password) {
			type(FIELD_LOGIN, username);
			type(FIELD_PASSWORD, password);
			submit(FIELD_PASSWORD);
			waitForPageLoaded();
		}
	
}
