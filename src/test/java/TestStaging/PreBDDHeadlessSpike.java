package TestStaging;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.guardian.PageObjects.AboutYouPage;
import com.guardian.PageObjects.AboutYouPageTry;
import com.guardian.utilities.myUtil;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.PageFactory;

public class PreBDDHeadlessSpike {
	
public WebDriver driver;
	
	
	@BeforeTest
	public void SetUp(){
		
		switch (myUtil.browserName) {
//		case "FireFox":
////			 ProfilesIni profile = new ProfilesIni();
////	         FirefoxProfile myprofile = profile.getProfile("LaxmiQAProfile");
////	        myprofile.setPreference("network.http.use-cache",false);
////	        myprofile.setPreference("browser.cache.disk.enable", false);
////	        myprofile.setPreference("browser.cache.memory.enable", false);
////			WebDriver driver = new FirefoxDriver(myprofile);
//			
//			break;
			
		case "Headless":
			driver = new HtmlUnitDriver();
			
			break;	
			
//		case "Chrome":
//			System.setProperty("webdriver.chrome.driver", "/Users/laxmisomni/Documents/Selenium/ChromeDriver/ChromeDriver");
//			driver=new ChromeDriver();
//			
//			break;
//		default :
//			driver = new FirefoxDriver();
//			
//			break;
			
		}
		
		
		
		
	}
	
	@Test()
	public void HeadlessTest() throws InterruptedException, Exception{
		
		driver.get(myUtil.BASE_URL);
		
		driver.manage().timeouts().implicitlyWait(35, TimeUnit.SECONDS);
	
		//Verify if you landed on correct page
//		Assert.assertTrue(driver.getCurrentUrl().equalsIgnoreCase(myUtil.BASE_URL),"\n**Landed on the Homepage**\n");
//		//Assert.assertTrue("\n**Landed on the Homepage**\n", driver.getCurrentUrl().equalsIgnoreCase(myUtil.BASE_URL));
//		if (driver.getCurrentUrl().contains(myUtil.BASE_URL)){
//			Reporter.log("**URL mateched as per expectation**\n");
//		}
		
		if(driver.getTitle().contains("Quote")){
			System.out.println("\n Landed successfully on the Landing Page");
			Reporter.log("\n Landed successfully on the Landing Page");
			
		}
		
		WebElement e= driver.findElement(By.id("FirstName"));
		e.sendKeys("Dummy_Test_text");
//		driver.findElement(By.id("LastName")).sendKeys("Dummy_Test_text");
		
//		AboutYouPageTry aboutYouPageTry = new AboutYouPageTry(driver);
//		//driver.manage().timeouts().implicitlyWait(55, TimeUnit.SECONDS);
//		aboutYouPageTry.datafillingtest();
		
		
	}
	
	@AfterTest
	public void TestClosure(){
		driver.close();
		driver.quit();
	}

}
