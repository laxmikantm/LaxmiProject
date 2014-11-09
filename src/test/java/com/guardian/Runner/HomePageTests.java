package com.guardian.Runner;

import static org.monte.media.FormatKeys.EncodingKey;
import static org.monte.media.FormatKeys.FrameRateKey;
import static org.monte.media.FormatKeys.KeyFrameIntervalKey;
import static org.monte.media.FormatKeys.MIME_QUICKTIME;
import static org.monte.media.FormatKeys.MediaTypeKey;
import static org.monte.media.FormatKeys.MimeTypeKey;
import static org.monte.media.VideoFormatKeys.CompressorNameKey;
import static org.monte.media.VideoFormatKeys.DepthKey;
import static org.monte.media.VideoFormatKeys.ENCODING_QUICKTIME_JPEG;
import static org.monte.media.VideoFormatKeys.QualityKey;

import java.awt.AWTException;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.io.IOException;

import junit.framework.Assert;

import org.monte.media.Format;
import org.monte.media.FormatKeys.MediaType;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.guardian.PageObjects.LandingPage;
import com.guardian.utilities.XMLDataReader;
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
	private XMLDataReader xmlDataReader;
	ScreenRecorder screenRecorder;
	String uEmail,passwd;
	

	
	@Before
	public void setUp() throws IOException, AWTException{
		xmlDataReader=new XMLDataReader();
		uEmail="";
		passwd="";


		
		switch (myUtil.browserName) {
		case "FireFox":
			driver = new FirefoxDriver();			
			break;
			
		case "Chrome":
			//System.setProperty("webdriver.chrome.driver", System.getProperty("user.home")+"/Documents/Selenium/ChromeDriver/ChromeDriver");
			System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"/ChromeDriver");
			driver=new ChromeDriver();			
			break;
			
		case "IE":
			System.setProperty("webdriver.ie.driver", System.getProperty("user.home")+"/drivers/IEDriverServer.exe");		 
		     driver = new InternetExplorerDriver();
		     break;
			
		default :
			driver = new FirefoxDriver();		
			break;
	}
		
		driver.get(myUtil.BASE_URL);
		driver.manage().window().maximize();
	}//end Before
	

	
	
	@Given("^the user accesses the login page of the Guardian website$")
	public void the_user_accesses_the_login_page_of_Guardian_website() throws Throwable {
	
		landingPage = new LandingPage(driver);
		landingPage.OpenLoginInterface();
		
	}

	@When("^the user enters value (.*) in username field$")
	public void the_user_enters_value_in_username_field(String username) throws Throwable {
		uEmail="";
		landingPage = new LandingPage(driver);
		
		if(username.equalsIgnoreCase("IncorrectUsername")){
			uEmail=xmlDataReader.searchElement("invalidUser", "email");

		}else if(username.equalsIgnoreCase("CorrectUsername")){

			uEmail=xmlDataReader.searchElement("validUser", "email");
				
		}
		landingPage.EnterUsername(uEmail);
		

		
	}

	@When("^the user enters value (.*) in password field$")
	public void the_user_enters_value_in_password_field(String password) throws Throwable {
		
		if(password.equalsIgnoreCase("InvalidPassword")){
			passwd=xmlDataReader.searchElement("invalidUser", "password");
		}else if(password.equalsIgnoreCase("CorrectPassword")){
			passwd=xmlDataReader.searchElement("validUser", "password");				
		}

		landingPage.EnterPassword(passwd);
	}

	@When("^Signs himself or herself in$")
	public void clicks_enter() throws Throwable {
		landingPage.Submit();
	}

	@Then("^the unsuccessful should be shown$")
	public void the_unsuccessful_should_be_shown() throws Throwable {
			org.junit.Assert.assertTrue("Invalid Login Check", landingPage.InvaliLoginCheck());
	}



	@Then("^the successful should be shown$")
	public void the_successful_should_be_shown() throws Throwable {
		String expectedUname= xmlDataReader.searchElement("validUser", uEmail);
		org.junit.Assert.assertTrue("Valid Login Check", landingPage.ValidLoginCheck(expectedUname));
		landingPage.LogOutAction();
	}	
		
	
	
	@After
	public void closure() throws IOException{
		driver.close();
		driver.quit();
	}

}
