package com.guardian.Runner.step_definitions;

import junit.framework.Assert;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.guardian.PageObjects.LandingPage;
import com.guardian.utilities.myUtil;

import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class HomePageTests {
	public WebDriver driver;
	LandingPage landingPage;
	
	@Before
	public void setUp(){
		switch (myUtil.browserName) {
		case "FireFox":
//			 ProfilesIni profile = new ProfilesIni();
//	         FirefoxProfile myprofile = profile.getProfile("LaxmiQAProfile");
//	        myprofile.setPreference("network.http.use-cache",false);
//	        myprofile.setPreference("browser.cache.disk.enable", false);
//	        myprofile.setPreference("browser.cache.memory.enable", false);
			driver = new FirefoxDriver();
			
			break;
			
		case "Headless":
			 driver = new HtmlUnitDriver();
			
			break;	
			
		case "Chrome":
			System.setProperty("webdriver.chrome.driver", "/Users/laxmisomni/Documents/Selenium/ChromeDriver/ChromeDriver");
			driver=new ChromeDriver();
			
			break;
			
		default :
			driver = new FirefoxDriver();
			
			break;
	}
	}//end Before
	

	
	@Given("^the user accesses the login page of Guardian website$")
	public void the_user_accesses_the_login_page_of_Guardian_website() throws Throwable {
	
		driver.get(myUtil.BASE_URL);
		driver.manage().window().maximize();
		
		landingPage = new LandingPage(driver);
		landingPage.OpenLoginInterface();
		
	}

	@When("^the user enters value (.*) in username field$")
	public void the_user_enters_value_in_username_field(String username) throws Throwable {
			landingPage.EnterUsername(username);
		
	}

	@When("^the user enters value (.*) in password field$")
	public void the_user_enters_value_InvalidPassword_in_password_field(String password) throws Throwable {
		landingPage.EnterPassword(password);
	}

	@When("^clicks enter$")
	public void clicks_enter() throws Throwable {
		landingPage.Submit();
	}

	@Then("^the unsuccessful should be shown$")
	public void the_unsuccessful_should_be_shown() throws Throwable {
		if(landingPage.LoginSuccessCheck(expectedUserName) == 1){
			System.out.println("Login UnSuccessful");
		}
	}



	@Then("^the successful should be shown$")
	public void the_successful_should_be_shown() throws Throwable {
		if(landingPage.SuccessfulChk() == 2){
			System.out.println("Login Successful");
		}
	
		
	}
	
	
	
	@After
	public void closure(){
		driver.close();
		driver.quit();
	}

}
