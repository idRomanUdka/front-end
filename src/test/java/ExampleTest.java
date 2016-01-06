import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(WebDriverListener.class)
public class ExampleTest extends WebDriverController {

	private String pageUrl = "admin#local-library";

	//CAS
	private static final By FIELD_LOGIN = By.name("username");
	private static final By FIELD_PASSWORD = By.name("password");
	
	//SEARCH FIELD
	private static final By FIELD_SEARCH = By.name("query");
	
    //Tabs
    private static final By TAB_SUBSCRIPTIONS = By.cssSelector("a[href='#your-holdings']");
    private static final By TAB_REPORTS = By.cssSelector("a[href='#reports']");
    
    //SEARCH RESULTS
    private static final By IMG_LOADING = By.cssSelector(".loading");

    @BeforeTest
    public void login(){
        openUrlInApp(pageUrl);
        addCookie("RS.LOCATION", "2519ca7b-4a4c-4f9d-908f-c2c755598618");
        login("pivaniushyn@copyright.com", "123456");
        addCookie("RS.LOCATION", "2519ca7b-4a4c-4f9d-908f-c2c755598618");
    }
    
    protected void login(String username, String password) {
		type(FIELD_LOGIN, username);
		type(FIELD_PASSWORD, password);
		submit(FIELD_PASSWORD);
		waitForPageLoaded();
	}

	@Test
    public void testSearch(){
        openUrlInApp("/");
        makeScreenshot();
        type(FIELD_SEARCH, "soccer");
        submit(FIELD_SEARCH);
        waitForPageLoaded();
        waitWhileElementIsPresent(IMG_LOADING);
        makeScreenshot();
    }
    
    @Test
    public void testLogin(){
        openUrlInApp(pageUrl);
        makeScreenshot();
    }
}
