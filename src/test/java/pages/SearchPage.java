package pages;

import org.openqa.selenium.By;

public class SearchPage extends WebDriverController{

	//SEARCH FIELD
	private static final By FIELD_SEARCH = By.name("query");
	
	 //SEARCH RESULTS
    private static final By IMG_LOADING = By.cssSelector(".loading");
	
	 public void search(String searchText) {
		 type(FIELD_SEARCH, searchText);
	     submit(FIELD_SEARCH);
	     waitForPageLoaded();
	     waitWhileElementIsPresent(IMG_LOADING);
	 }
	
}
