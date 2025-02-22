package com.rightfind.test;
import org.openqa.selenium.By;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import pages.WebDriverController;
import utils.WebDriverListener;

@Listeners(WebDriverListener.class)
public class ScreenTest extends WebDriverController{
	
	//URLS
	private String urlDigitalLibrary = "admin#local-library";
	private String urlSearch = "/search#all/soccer";
	private String urlCart = "/search#shopping-cart/";
	private String urlViewOrders = "/manage_account#view-orders";
	private String urlAccountSettings = "/manage_account#account-settings";
	private String urlManageTags = "/search#manage-tags";
	private String urlAdvancedSearch = "/#advanced-search/journal";
	private String urlSubscriptions = "/admin#your-holdings/holdings/list";
	private String urlSubscriptionsPlatforms = "/admin#your-holdings/subscriptions";
	private String urlReportsOrders = "/admin#reports/orders-reports";
	private String urlReportsSubscriptionsUsage = "/admin#reports/holdings-reports";
	private String urlReportsSubscriptionsCosts = "/admin#reports/costs-reports/subscriptions-costs";
	private String urlReportsJournalCosts = "/admin#reports/costs-reports/journals-costs";
	private String urlReportsDocumentDeliveryCosts = "/admin#reports/costs-reports/document-delivery-costs";
	private String urlPendingRequests = "/admin#approval-requests/pending-requests";
	private String urlApprovedRequests = "/admin#approval-requests/approved-requests";
	private String urlDeniedRequests = "/admin#approval-requests/denied-requests";
	private String urlAgreementsList = "/admin#agreements/agreements-list";
	private String urlCollections = "/admin#agreements/agreements-collections";
	private String urlRights = "/admin#agreements/rights-categories";
	private String urlStandardTerms = "/admin#agreements/standard-terms";
	private String urlCustomMessaging = "/admin#custom-messaging";
	private String urlArticleDetails = "/search#detailed-article/id=216724405";
	
	//LOGIN FORM
	private static final By FIELD_LOGIN = By.name("username");
	private static final By FIELD_PASSWORD = By.name("password");
	
	//LOADER
    //private static final By IMG_LOADING = By.cssSelector(".loading");
	private static final By IMG_LOADING = By.cssSelector("img[src*='loader']");
	
	//DL delete button
	private static final By BUTTON_DELETE_SELECTED = By.cssSelector(".btn-delete-selected");
	
	//Reports delete button
	private static final By BUTTON_DOWNLOAD_FULL_REPORT = By.cssSelector(".btn-download");
	
	//GREY DIV WHEN CONTENT IS LOADING
	private static final By DIV_LOADING = By.cssSelector(".blockUI");
	
    //SEARCH FIELD
  	private static final By FIELD_SEARCH = By.name("query");
  	
  	//DIV article-details
  	private static final By DIV_ARTICLE_DETAILS = By.id("article-details");
	private static final By LINK_HOW_CAN_I_USE = By.cssSelector(".rights-dialog");
	private static final By MODAL_ARTICLE_PARENTS_DETAILS  = By.cssSelector(".modal-content");

    @BeforeTest
    public void login(){
        openUrlInApp(urlDigitalLibrary);
        //windowSetSize(1600,900);
        addCookie("RS.LOCATION", "2519ca7b-4a4c-4f9d-908f-c2c755598618");
		type(FIELD_LOGIN, "pivaniushyn@copyright.com");
		type(FIELD_PASSWORD, "123456");
		submit(FIELD_PASSWORD);
		waitForPageLoaded();
        setItemInSessionStorage("isTooltipWasClosed", "yes");
        addCookie("RS.LOCATION", "2519ca7b-4a4c-4f9d-908f-c2c755598618");
    }

	@Test
    public void testSearch(){
        openUrlInApp("/");
        makeScreenshot();
        openUrlInApp(urlSearch);
        validateElementIsNotVisibleAlready(IMG_LOADING);
        makeScreenshot();
    }
    
    @Test
    public void testDigitalLibrary(){
        openUrlInApp(urlDigitalLibrary);
        validateElementVisible(BUTTON_DELETE_SELECTED);
        makeScreenshot();
    }
    
    @Test
    public void testCart(){
        openUrlInApp(urlCart);
        validateElementIsNotVisibleAlready(IMG_LOADING);
        makeScreenshot();
    }
    
    @Test
    public void testAccountSettings(){
        openUrlInApp(urlAccountSettings);
        makeScreenshot();
        openUrlInApp(urlViewOrders);
        makeScreenshot();
    }
    
    @Test
    public void testManageTags(){
        openUrlInApp(urlManageTags);
        makeScreenshot();
    }
    
    @Test
    public void testAdvancedSearch(){
        openUrlInApp(urlAdvancedSearch);
        makeScreenshot();
    }

    @Test
    public void testSubscriptions(){
    	 openUrlInApp(urlSubscriptions);
         makeScreenshot();
         openUrlInApp(urlSubscriptionsPlatforms);
         makeScreenshot();
         openUrlInApp(urlSubscriptions);
         makeScreenshot();
    }

    @Test
    public void testReport(){
        openUrlInApp(urlReportsOrders);
        makeScreenshot();
        openUrlInApp(urlReportsSubscriptionsCosts);
        validateElementIsNotVisibleAlready(DIV_LOADING);
        validateElementVisible(BUTTON_DOWNLOAD_FULL_REPORT);
        makeScreenshot();
        openUrlInApp(urlReportsSubscriptionsUsage);
        validateElementIsNotVisibleAlready(DIV_LOADING);
        makeScreenshot();
        openUrlInApp(urlReportsJournalCosts);
        validateElementIsNotVisibleAlready(DIV_LOADING);
        validateElementVisible(BUTTON_DOWNLOAD_FULL_REPORT);
        makeScreenshot();
        openUrlInApp(urlReportsDocumentDeliveryCosts);
        validateElementVisible(BUTTON_DOWNLOAD_FULL_REPORT);
        makeScreenshot();
    }

    @Test
    public void testRequests(){
        openUrlInApp(urlPendingRequests);
        makeScreenshot();
        openUrlInApp(urlApprovedRequests);
        makeScreenshot();
        openUrlInApp(urlDeniedRequests);
        makeScreenshot();
    }

    @Test
    public void testAgreement(){
        openUrlInApp(urlAgreementsList);
        makeScreenshot();
        openUrlInApp(urlCollections);
        makeScreenshot();
        openUrlInApp(urlRights);
        makeScreenshot();
        openUrlInApp(urlStandardTerms);
        makeScreenshot();
    }

    @Test
    public void testCustomMessaging(){
        openUrlInApp(urlCustomMessaging);
        makeScreenshot();
    }

    @Test
    public void testArticleDetails(){
        openUrlInApp(urlArticleDetails);
        validateElementIsNotVisibleAlready(IMG_LOADING);
	    makeScreenshot();
	    click(LINK_HOW_CAN_I_USE);
	    validateElementVisible(MODAL_ARTICLE_PARENTS_DETAILS);
	    makeScreenshot();
    }
    
}
