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
public class ExampleTest {

	private String pageUrl = "admin#local-library";


    @BeforeTest
    public void login(){
        LoginPage lp = new LoginPage();
        lp.openUrlInApp(pageUrl);
        lp.addCookie("RS.LOCATION", "2519ca7b-4a4c-4f9d-908f-c2c755598618");
        lp.login("pivaniushyn@copyright.com", "123456");
        lp.addCookie("RS.LOCATION", "2519ca7b-4a4c-4f9d-908f-c2c755598618");
    }

	@Test
    public void testSearch(){
        SearchPage sp = new SearchPage();
		sp.openUrlInApp("/");
        sp.makeScreenshot();
        sp.search("soccer");
        sp.makeScreenshot();
    }
    
    @Test
    public void testLogin(){
        LoginPage lp = new LoginPage();
        lp.openUrlInApp(pageUrl);
        lp.makeScreenshot();
    }
}
