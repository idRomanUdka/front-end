package com.rightfind.test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import pages.LoginPage;
import pages.SearchPage;
import pages.WebDriverController;
import utils.WebDriverListener;

@Listeners(WebDriverListener.class)
public class ScreenTest extends WebDriverController{
	
	//URLS
	private String urlDigitalLibrary = "admin#local-library";
	private String urlSearch = "/rs-ui-web/search#all/soccer";
	
	//LOGIN FORM
	private static final By FIELD_LOGIN = By.name("username");
	private static final By FIELD_PASSWORD = By.name("password");
	
	//LOADER
    private static final By IMG_LOADING = By.cssSelector(".loading");

    @BeforeTest
    public void login(){
        openUrlInApp(urlDigitalLibrary);
        addCookie("RS.LOCATION", "2519ca7b-4a4c-4f9d-908f-c2c755598618");
		type(FIELD_LOGIN, "pivaniushyn@copyright.com");
		type(FIELD_PASSWORD, "123456");
		submit(FIELD_PASSWORD);
		waitForPageLoaded();
        addCookie("RS.LOCATION", "2519ca7b-4a4c-4f9d-908f-c2c755598618");
    }

	@Test
    public void testSearch(){
        openUrlInApp("/");
        makeScreenshot();
        openUrlInApp(urlSearch);
	    waitForPageLoaded();
	    waitWhileElementIsPresent(IMG_LOADING);
        makeScreenshot();
    }
    
    @Test
    public void testLogin(){
        openUrlInApp(urlDigitalLibrary);
        makeScreenshot();
    }
}
