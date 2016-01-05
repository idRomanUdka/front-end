import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ExampleTest extends WebDriverController {

	private String pageUrl = "admin#local-library";

	//CAS
	private static final By FIELD_LOGIN = By.name("username");
	private static final By FIELD_PASSWORD = By.name("password");
    
	
    //Tabs
    private static final By TAB_SUBSCRIPTIONS = By.cssSelector("a[href='#your-holdings']");
    private static final By TAB_REPORTS = By.cssSelector("a[href='#reports']");

    @BeforeTest
    public void login(){
        openUrlInApp(pageUrl);
        login("pivaniushyn@copyright.com", "123456");
    }
    
    protected void login(String username, String password) {
		type(FIELD_LOGIN, username);
		type(FIELD_PASSWORD, password);
		submit(FIELD_PASSWORD);
		waitForPageLoaded();
	}

	@Test
    public void testScreens(){
        openUrlInApp("/");
        makeScreenshot();
        //click(TAB_SUBSCRIPTIONS);
        //click(TAB_REPORTS);
    }
    
    @Test
    public void testLogin(){
        openUrlInApp(pageUrl);
        makeScreenshot();
    }
}
