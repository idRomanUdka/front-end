
import com.googlecode.fightinglayoutbugs.FightingLayoutBugs;
import com.googlecode.fightinglayoutbugs.LayoutBug;
import com.googlecode.fightinglayoutbugs.WebPage;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * The type Web driver controller.
 */
public class WebDriverController {
    public WebDriver driver;
    public WebDriver driverChecking;
    private static final int SCRIPT_TIMEOUT = 30;
    private WebDriverWait waitDriver;

    protected int count = 0;
    private String pathToCheckingScreens = "";
    private String pathToSampleScreens = "";
    private String pathToDiffScreens = "";
    String browser = "";
    String browserProp = getProperties("browser");
    String flagFirebug = getProperties("flagFirebug");
    String sampleUrl = "";
    String checkingUrl = "";
    static List<String> failedTests = new ArrayList<String>();


    
    /**
     * The constant log.
     */
    protected static Logger log = Logger.getLogger(WebDriverController.class);

    public WebDriverController() {
        browser = System.getenv("browser");
        if (browser == null)
            browser = browserProp;
    }
    
    @AfterSuite
    public void deleteFile() {
    	//deleteFileInDirectory("screenshots\\checking");
    	//deleteFileInDirectory("screenshots\\sample");
    }

    @AfterMethod
    public void CheckScreens() {
	            for (int i = 0; i < count; i++) {
	                CheckingDifferentImages.checkDifference(pathToCheckingScreens + "\\" + i + ".png",
	                		pathToSampleScreens + "\\" + i + ".png",
	                		pathToDiffScreens + "_diff_" + i + ".png", 1);
	            }
        count = 0;
    }


    
    private static void deleteFileInDirectory(String path_from) {
        File f = new File(path_from);
        if (f.isDirectory()) {
            String[] child = f.list();
            if (child.length != 0)
                for (int i = 0; i < child.length; i++) {
                    delete(path_from + "/" + child[i]);
                }
        } else
            System.out.println("This isn't a diractory");
    }

    private static void delete(String path_from) {
        File f = new File(path_from);
        if (f.isDirectory()) {
            String[] child = f.list();
            for (int i = 0; i < child.length; i++) {
                delete(path_from + "/" + child[i]);
            }
            f.delete();
        } else {
            f.delete();
        }
    }
    
    
    @BeforeSuite
    public void initBrowser() {
    	if (driver == null){
    		setBrowser();
        	sampleUrl = System.getenv("sampleUrl");
        	if (sampleUrl == null) {
        		sampleUrl = getProperties("sampleUrl");
        	}
        	checkingUrl = System.getenv("checkingUrl");
        	if (checkingUrl == null) {
        		checkingUrl = getProperties("checkingUrl");
        	}
    	}
    }

    @BeforeMethod
    public void setConditions(Method method) {{

        pathToCheckingScreens = "screenshots\\checking\\" + method.getDeclaringClass().getName() + "\\" + browser + "\\" + method.getName();
        pathToSampleScreens = pathToCheckingScreens.replaceAll("checking", "sample");
    	pathToDiffScreens = pathToCheckingScreens.replaceAll("checking", "diff");
    	}
        
    }

    @AfterSuite
    public void checkTests() {
        shutdown();
        if (!failedTests.isEmpty()) {
            for (String s : failedTests)
                log.error(s);
            Assert.fail("There was errors in tests");
        }
    }

    /**
     * Get properties.
     *
     * @param param the param
     * @return the string
     */
    public String getProperties(String param) {
        Properties prop = new Properties();
        try {
            prop.load(new FileReader("application.properties"));
        } catch (IOException e) {
            Assert.fail("There is no file with properties");
        }
        return prop.getProperty(param);
    }


    private void setBrowser() {
       if ("firefox".equals(browser)) {
                try {
                    String versionFirebug = getProperties("firebug-version");
                    FirefoxProfile firefoxProfile = new FirefoxProfile();
                    if (getProperties("firebug").equals("true")) {
                        firefoxProfile.addExtension(
                                new File("src\\test\\extensions\\firebug-" + versionFirebug + ".xpi"));
                        firefoxProfile.setPreference("extensions.firebug.currentVersion",
                                versionFirebug); // Avoid startup screen
                    }
                    driver = new FirefoxDriver(firefoxProfile);
                    driverChecking = new FirefoxDriver(firefoxProfile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if ("ie".equals(browser)) {
                try {
                    System.setProperty("webdriver.ie.driver", "lib\\IEDriverServer64.exe");
                    DesiredCapabilities capabilitiesIe = DesiredCapabilities.internetExplorer();
                    capabilitiesIe.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                    driver = new InternetExplorerDriver(capabilitiesIe);
                    driverChecking = new InternetExplorerDriver(capabilitiesIe);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if ("chrome".equals(browser)) {
                try {
                    System.setProperty("webdriver.chrome.driver", "lib\\chromedriver.exe");
                    ChromeOptions options = new ChromeOptions();
                    String browserLanguage = "--lang=en";
                    options.addArguments(browserLanguage);
                    driver = new ChromeDriver(options);
                    driverChecking = new ChromeDriver(options);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        driver.manage().timeouts().setScriptTimeout(SCRIPT_TIMEOUT, TimeUnit.SECONDS);
        maximizeWindow();
    }

    public void clickAtLocationOf(By element) {
        Actions action = new Actions(driver);

        action.moveToElement(findElement(element)).build().perform();
        action.click().build().perform();
    }

    /**
     * Wait for page loaded.
     *
     * @param driver the driver
     */
    public void waitForPageLoaded() {
        ExpectedCondition<Boolean> expectation = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        String resultScript = String.valueOf(
                                ((JavascriptExecutor) driver).executeScript("return document.readyState"));
                        return resultScript.equals("complete");
                    }
                };
        WebDriverWait wait = new WebDriverWait(driver, 60);
        try {
            wait.until(expectation);
        } catch (Throwable error) {
            Assert.fail("Timeout waiting for Page Load Request to complete.");
        }
    }

    public boolean waitForScriptLoaded(String src) {
        for (int i = 0; i < SCRIPT_TIMEOUT; i++) {
            Long jsLoaded = (Long) executeScript("return $('script[src*=\"" + src + "\"]').length;");
            if (jsLoaded != 0) {
                log.info(jsLoaded);
                return true;
            }
            sendPause(1);
            log.info(jsLoaded);
        }

        return false; //must be false
    }

    public void maximizeWindow() {
    	driverChecking.manage().window().maximize();
    	driver.manage().window().maximize();
    }

    /**
     * Gets instance wait driver.
     }

     /**
     * Maximize window.
     *
     * @return the instance wait driver
     */


    /**
     * Gets page adress.
     *
     * @return the page adress
     */
    public String getPageAdress() {
        return driver.getCurrentUrl();
    }

    /**
     * Go to url.
     *
     * @param url the url
     */
    public void goToUrl(String url) {
        driver.get(url);
    }

    /**
     * Open url in the app
     *
     * @param subUrl the sub url
     */
    public void openUrlInApp(String subUrl) {
        if (subUrl.length() > 0 && subUrl.indexOf("/") == 0){
        	driverChecking.get(checkingUrl + subUrl.substring(1));
        	driver.get(sampleUrl + subUrl.substring(1));
        }else{
    		driverChecking.get(checkingUrl + subUrl);
        	driver.get(sampleUrl + subUrl);
        }
    }

    /**
     * Get alert text.
     *
     * @return the string
     */
    protected String getAlertText() {
        // Get a handle to the open alert, prompt or confirmation
        Alert alert = driver.switchTo().alert();
        // Get the text of the alert or prompt
        return alert.getText();
    }

    /**
     * Click ok in alert.
     */
    protected void clickOkInAlert() {
        // Get a handle to the open alert, prompt or confirmation
        Alert alert = driver.switchTo().alert();
        // Get the text of the alert or prompt
        log.debug("alert: " + alert.getText());
        // And acknowledge the alert (equivalent to clicking "OK")
        alert.accept();
    }

    /**
     * Window set size.
     *
     * @param widthWindow  the width window
     * @param heightWindow the height window
     */
    public void windowSetSize(int widthWindow, int heightWindow) {
        java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (((screenSize.width >= widthWindow) && (screenSize.height >= heightWindow)) ||
                ((0 >= widthWindow) && (0 >= heightWindow))) {
            Dimension targetSize = new Dimension(widthWindow, heightWindow);
            WebDriver.Window window = driver.manage().window();
            Dimension size = window.getSize();
            log.debug("Current windowSize = " + size);
            window.setSize(targetSize);
            log.debug("New windowSize = " + size);
        } else {
            log.debug("it is impossible");
        }
    }

    /**
     * Wait for element present.
     *
     * @param by the by
     * @return the boolean
     */
    public boolean waitForElementPresent(By by) {
        
        try{
        	new WebDriverWait(driver, 15).until(ExpectedConditions.presenceOfElementLocated(by));
        }catch(RuntimeException e){
        	return false;
        }
    	return true;

    }

    protected boolean assertVisible(By by) {
        for (int i = 0; i < SCRIPT_TIMEOUT; i++) {
            if (isVisible(by)) {
                return true;
            }
            sendPause(1);
        }
        Assert.fail("Element " + by + " is not visible");
        return false;
    }


    /**
     * Wait element for sec.
     *
     * @param by      the by
     * @param seconds the seconds
     */
    public void waitElementForSec(By by, int seconds) {
        for (int i = 0; i < seconds; i++) {
            try {
                if (findElement(by).isDisplayed()) {
                    sendPause(1);
                    break;
                } else {
                    sendPause(1);
                }
            } catch (Exception e) {
            }
        }

    }

    /**
     * Wait staleness element.
     *
     * @param by the by
     */
    public void waitStalenessElement(final By by) {
        new WebDriverWait(driver, 15).until(new ExpectedCondition<Object>() {
            @Override
            public Object apply(WebDriver webDriver) {
                try {
                    final WebElement element = driver.findElement(by);
                    if (element != null && element.isDisplayed()) {
                        return element;
                    }
                } catch (StaleElementReferenceException e) {

                }
                return null;
            }
        });
    }

    public boolean validateElementInvisible(By by) {
        
        try{
        	new WebDriverWait(driver, 30).until(ExpectedConditions.invisibilityOfElementLocated(by));
        }catch(RuntimeException e){
        	return false;
        }
    	return true;

    }

    /**
     * Click on staleness element.
     *
     * @param by the by
     */
    public void clickOnStalenessElement(final By by) {

        new WebDriverWait(driver, 15).until(new ExpectedCondition<Object>() {
            @Override
            public Object apply(WebDriver webDriver) {
                try {
                    final WebElement element = driver.findElement(by);
                    if (element != null && element.isDisplayed() && element.isEnabled()) {
                        element.click();
                        return element;
                    }
                } catch (StaleElementReferenceException e) {
                }
                return null;
            }
        });
    }

    /**
     * Click cancel in alert.
     */
    protected void clickCancelInAlert() {
        // Get a handle to the open alert, prompt or confirmation
        Alert alert = driver.switchTo().alert();
        // Get the text of the alert or prompt
        log.debug("alert: " + alert.getText());
        // And acknowledge the alert (equivalent to clicking "Cancel")
        alert.dismiss();
    }

    /**
     * Wait for not attribute.
     *
     * @param by        the by
     * @param attribute the attribute
     * @param value     the value
     */
    protected void waitForNotAttribute(final By by,
            final String attribute,
            final String value) {
        waitDriver.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver d) {
                return d.findElement(by).getAttribute(attribute).equals(value);
            }
        });
    }


    /**
     * Wait for text present.
     *
     * @param text the text
     */
    protected void waitForTextPresent(final String text) {
        waitDriver.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver d) {
                return d.getPageSource().contains(text);
            }
        });
    }

    /**
     * Send pause.
     *
     * @param sec the sec
     */
    public static void sendPause(int sec) {
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException iex) {
            Thread.interrupted();
        }
    }


    /**
     * Check resource by get request.
     *
     * @param requestURL the request uRL
     * @return the boolean
     */
    public boolean checkResourceByGetRequest(String requestURL) {
        HttpURLConnection httpConn = null;
        URL url = null;
        boolean result = false;
        try {
            url = new URL(requestURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            httpConn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        httpConn.setUseCaches(false);
        httpConn.setDoInput(true); // true if we want to read server's response
        httpConn.setDoOutput(false); // false indicates this is a GET request
        try {
            if (httpConn.getResponseCode() == 200 && httpConn.getResponseMessage().equals("OK"))
                result = true;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }

    /**
     * Select value in drop down.
     *
     * @param by          the by
     * @param optionValue the option value
     */
    protected void selectValueInDropDown(By by, String optionValue) {
        Select select = new Select(driver.findElement(by));
        select.selectByValue(optionValue);
    }

    /**
     * Click hidden element
     *
     * @param className     - class of the element which will be clicked
     * @param numberElement - number of the element which will be clicked
     */
    protected void clickInvisibleElementByJs(String className, int numberElement) {
        executeScript("document.getElementsByClassName('" + className + "')[" + numberElement + "].click();");
    }

    /**
     * Submit void.
     *
     * @param by the by
     */
    public void submit(By by) {
        waitForElementPresent(by);
        driverChecking.findElement(by).submit();
        driver.findElement(by).submit();

    }

    /**
     * Move to element.
     *
     * @param by the by
     */
    public void moveToElement(By by) {
        Actions actions = new Actions(driver);
        waitForElementPresent(by);
        actions.moveToElement(driver.findElement(by)).build().perform();
    }

    public Actions moveToElement(By by, int x, int y) {
        Actions actions = new Actions(driver);
        waitForElementPresent(by);
        actions.moveToElement(driver.findElement(by), x, y).build().perform();
        makeScreenshot();
        return actions;
    }

    public void moveByOffset(int x, int y) {
        Actions action = new Actions(driver);
        action.moveByOffset(x, y).build().perform();
        makeScreenshot();
    }

    /**
     * Click void.
     *
     * @param by the by
     */
    public void click(By by) {
        log.debug("Click : ");
        waitForPageLoaded();
        assertVisible(by);
        driver.findElement(by).click();
        waitForPageLoaded();
        makeScreenshot();
    }


    /**
     * Gets text.
     *
     * @param by the by
     * @return the text
     */
    public String getText(By by) {
        log.debug("Text from: ");
        assertVisible(by);
        return driver.findElement(by).getText();
    }


    /**
     * Gets attribute.
     *
     * @param by            the by
     * @param nameAttribute the name attribute
     * @return the attribute
     */
    public String getAttribute(By by, String nameAttribute) {
        assertVisible(by);
        return driver.findElement(by).getAttribute(nameAttribute);
    }


    /**
     * Gets page source.
     *
     * @return the page source
     */
    public String getPageSource() {
        return driver.getPageSource();
    }


    /**
     * Select window.
     *
     * @param windowId the window id
     */
    protected void selectWindow(String windowId) {
        for (String handle : driver.getWindowHandles()) {
            if (handle.equals(windowId)) {
                switchToWindow(handle);
                break;
            }
        }
    }

    /**
     * Select other window.
     */
    protected void selectOtherWindow() {
        String current = driver.getWindowHandle();
        int timer = 0;
        while (timer < SCRIPT_TIMEOUT) {
            if (driver.getWindowHandles().size() > 1)
                break;
            else {
                sendPause(1);
                timer++;
            }

        }
        for (String handle : driver.getWindowHandles()) {
            try {
                if (handle != current)
                    switchToWindow(handle);
            } catch (Exception e) {

                Assert.fail("Unable to select window");
            }
        }


    }


    /**
     * Types text to element
     *
     * @param by       the by
     * @param someText - text, which should be typing
     */
    public void type(By by, String someText) {
        log.debug("Type:" + someText + " to:");
        validateElementVisible(by);
        driverChecking.findElement(by).clear();
        driverChecking.findElement(by).sendKeys(someText);
        
        driver.findElement(by).clear();
        driver.findElement(by).sendKeys(someText);
    }


    /**
     * Type void.
     *
     * @param element  the element
     * @param someText the some text
     */
    public void type(WebElement element, String someText) {
        log.debug("Type:" + someText + " to:" + element.getText());
        element.clear();
        element.sendKeys(someText);
    }

    /**
     * Make screenshot.
     */
    public void makeScreenshot() {
        File scrFileChecking;
        File scrFileSample;
        sendPause(5);
        try {
            scrFileChecking = ((TakesScreenshot) driverChecking).getScreenshotAs(OutputType.FILE);
            scrFileSample = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            int i = count++;
			FileUtils.copyFile(scrFileSample, new File(pathToSampleScreens + "\\" + i + ".png"));
            FileUtils.copyFile(scrFileChecking, new File(pathToCheckingScreens + "\\" + i + ".png"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }


    /**
     * Validate element present.
     *
     * @param by the by
     * @return the boolean
     */
    public boolean validateElementPresent(By by) {
        for (int i = 0; i < SCRIPT_TIMEOUT; i++) {
            if (isElementPresent(by)) {
                return true;
            }
            sendPause(1);
        }

        return false;
    }

    /**
     * Validates that element isn't visible
     *
     * @param by the by
     * @return boolean
     */
    public boolean validateElementIsNotVisible(By by) {
        for (int i = 0; i < SCRIPT_TIMEOUT; i++) {
            if (!isVisible(by))
                return true;
            sendPause(1);
        }
        return false;
    }

    /**
     * Validates that element is visible
     * returns true if by is visible
     * returns false if by is not visible for a while
     *
     * @param by - by of element contains xpath= or id= or css
     */
    public boolean validateElementVisible(By by) {
        
        try{
        	new WebDriverWait(driver, 15).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
        }catch(RuntimeException e){
        	return false;
        }
    	return true;

    }

    /**
     * elementToBeClickable(WebElement element)
     *An expectation for checking an element is visible and enabled such that you can click it.
     *
     */
    public boolean validateElementClickable(final By by) {
    	
        try{
        	new WebDriverWait(driver, 15).until(ExpectedConditions.elementToBeClickable(by));
        }catch(RuntimeException e){
        	return false;
        }
    	return true;
    }

    /**
     * Waits while element is present on the page
     *
     * @param by the by
     */
    public void waitWhileElementIsPresent(By by) {
    	waitForElementPresent(by);
        for (int i = 0; i < 30; i++) {
            if (getCountElements(by) == 0) {
                return;
            }
            sendPause(1);
        }
        throw new RuntimeException("The element is still present");
    }


    /**
     * Hovers on element
     *
     * @param by the by
     */
    public void hoverOn(By by) {
        Actions action = new Actions(driver);
        action.moveToElement(findElement(by)).build().perform();
        log.info("Action - hover on to locator: ");
    }

    /**
     * Show hover game in nav bar.
     *
     * @param serviceID the service iD
     */
    public void showHoverGameInNavBar(int serviceID) {
        executeScript(
                "$(\".bGamesNav__ePlaceholder__mId" + serviceID + " .bGamesNav__eItem__eBlock\").addClass(\"bGamesNav__eItem__eBlock__mHover\")");
    }


    /**
     * Scroll on top.
     */
    public void scrollOnTop() {
        executeScript("window.scrollTo(0,0)");
    }

    /**
     * Scroll on top.
     */
    public void scrollOnDawn(int i) {
        int k = 2000 * i;
        executeScript("window.scrollTo(" + k + "," + k + ")");
    }

    /**
     * Returns count of elements on a page with this locator
     *
     * @param by the by
     * @return the count elements
     */
    public int getCountElements(By by) {
        waitForElementPresent(by);
        return driver.findElements(by).size();
    }

    public void scrollBy(String params) {
        executeScript("window.scrollBy(" + params + ")");
    }

    public void scroll(String params) {
        executeScript("window.scroll(" + params + ")");
        sendPause(2);
    }

    /**
     * Returns WebDriver
     *
     * @return the driver
     */
    public WebDriver getDriver() {
        return driver;
    }
    
    /**
     * Returns WebDriver
     *
     * @return the driver
     */
    public WebDriver getCheckingDriver() {
        return driverChecking;
    }

    /**
     * Gets cookie.
     *
     * @param key the key
     * @return the cookie
     */
    public Cookie getCookie(String key) {
        Cookie cookie = driver.manage().getCookieNamed(key);
        return cookie;
    }

    /**
     * Gets cookies.
     *
     * @return the cookies
     */
    public Set<Cookie> getCookies() {
        Set<Cookie> cookies = driver.manage().getCookies();
        return cookies;
    }

    /**
     * Sends into a browser
     */
    public void navigationBack() {
        driver.navigate().back();
    }

    /**
     * Delete all cookies.
     *///Delete all cookies
    public void deleteAllCookies() {
        driver.manage().deleteAllCookies();
    }


    /**
     * Add cookie.
     *
     * @param key   the key
     * @param value the value
     */// Set a cookie
    public void addCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        driverChecking.manage().addCookie(cookie);
        driver.manage().addCookie(cookie);
    }

    /**
     * Find element.
     *
     * @param by the by
     * @return the web element
     */
    public WebElement findElement(final By by) {
        return driver.findElement(by);
    }

    /**
     * Find elements.
     *
     * @param by the by
     * @return the list
     */
    public List<WebElement> findElements(final By by) {
        return driver.findElements(by);
    }


    /**
     * Get void.
     *
     * @param url the url
     */
    public void get(final String url) {
        if (url.isEmpty() || url == null) {
            throw (new IllegalArgumentException());
        }
        driver.get(url);
    }

    /**
     * Is element present.
     *
     * @param by the by
     * @return the boolean
     */
    public boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        } catch (StaleElementReferenceException se) {
            return false;
        }
    }

    /**
     * Is visible.
     *
     * @param by the by
     * @return the boolean
     */
    public boolean isVisible(By by) {
        try {
            if (driver.findElements(by).size() > 0)
                if (driver.findElement(by).isDisplayed())
                    return true;
                else
                    return false;
        } catch (NoSuchElementException e) {
            return false;
        } catch (StaleElementReferenceException se) {
            return false;
        }
        return false;
    }

    /**
     * Is enable.
     *
     * @param by the by
     * @return the boolean
     */
    public boolean isEnable(By by) {
        try {
            if (driver.findElements(by).size() > 0)
                if (driver.findElement(by).isEnabled())
                    return true;
                else
                    return false;
        } catch (NoSuchElementException e) {
            return false;
        } catch (StaleElementReferenceException se) {
            return false;
        }
        return false;
    }

    /**
     * Shutdown void.
     */
    public void shutdown() {
        try {
            driver.quit();
            driverChecking.quit();
        } catch (Exception e) {
        }
        driver = null;
        driverChecking = null;
    }

    /**
     * Executes JavaScript in the context of the currently selected frame or window. (See also {@link JavascriptExecutor})
     *
     * @param script the script
     * @param args   the args
     * @return the object
     */
    public Object executeScript(String script, Object... args) {
        return ((JavascriptExecutor) driver).executeScript(script, args);
    }

    /**
     * Execute async script.
     *
     * @param script the script
     * @param args   the args
     * @return the object
     */
    public Object executeAsyncScript(String script, Object... args) {
        return ((JavascriptExecutor) driver).executeAsyncScript(script, args);
    }

    /**
     * Change cookie.
     *
     * @param key   the key
     * @param value the value
     */// Change the cookie
    public void changeCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        driver.manage().deleteCookieNamed(key);
        driver.manage().addCookie(cookie);
    }

    /**
     * Download file.
     *
     * @param path the path
     */
    public void downloadFile(String path) {

    }

    /**
     * Returns attribute value
     *
     * @param by        -
     * @param attribute -
     * @return the attribute value
     */
    public String getAttributeValue(By by, String attribute) {
        return driver.findElement(by).getAttribute(attribute);
    }

    /**
     * Refreshes current page
     */
    public void refresh() {
        driver.navigate().refresh();
    }

    /**
     * Switches to iFrame, if it needed
     *
     * @param iFrame - the name or id of iFrame
     */
    public void switchTo(String iFrame) {
        driver.switchTo().frame(iFrame);
    }

    /**
     * Switch to window.
     *
     * @param iFrame the i frame
     */
    public void switchToWindow(String iFrame) {
        driver.switchTo().window(iFrame);
    }


    /**
     * Returns link to main content on the page
     */
    public void switchToMainContent() {
        driver.switchTo().defaultContent();
    }

    public Point getLoc(By element) {
        return driver.findElement(element).getLocation();
    }
    
    /*
     public static void main( String[] args ) throws Exception {
        File file1 = new File( args[0] );
        File file2 = new File( args[1] );
 
        BufferedImage image1 = ImageIO.read( file1 );
        BufferedImage image2 = ImageIO.read( file2 );
 
        int x1 = image1.getWidth();
        int x2 = image2.getWidth();
        if ( x1 != x2 ) {
            System.out.println( "Widths are different: " + x1 + " != " + x2 );
            System.exit( 1 );
        }
 
        int y1 = image1.getHeight();
        int y2 = image2.getHeight();
        if ( y1 != y2 ) {
            System.out.println( "Heights are different: " + y1 + " != " + y2 );
            System.exit( 2 );
        }
 
        for ( int x = 0; x < x1; x++ ) {
            for ( int y = 0; y < y1; y++ ){
                int p1 = image1.getRGB( x, y );
                int p2 = image2.getRGB( x, y );
                if ( p1 != p2 ) {
                    System.out.println("Pixel is different at x/y " + x + "/" + y + ": " + p1 + " != " + p2 );
                    System.exit( 3 );
                }
            }
        }
 
        System.out.println( "Images are identical" );
        System.exit( 0 );
    }
     */

}
