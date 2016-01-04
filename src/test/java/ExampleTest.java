import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class ExampleTest extends WebDriverController {

	private String pageUrl = "admin#local-library";

    //tTabs
    private static final By TAB_SUBSCRIPTIONS = By.cssSelector("a[href='#your-holdings']");
    private static final By TAB_REPORTS = By.cssSelector("a[href='#reports']");

    @Test
    public void testScreens(){
        openUrlInApp(pageUrl);
        //click(TAB_SUBSCRIPTIONS);
        //click(TAB_REPORTS);
    }

}
