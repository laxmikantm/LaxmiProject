package TestStaging;



import java.awt.AWTException;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.text.Utilities;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;
import org.monte.media.Format;
import org.monte.screenrecorder.ScreenRecorder;

import static org.junit.Assert.assertEquals;
import static org.monte.media.AudioFormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;

import org.monte.media.math.Rational;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.monte.media.math.Rational;
import org.monte.media.Format;
import org.monte.screenrecorder.ScreenRecorder;

import static org.monte.media.AudioFormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;

import org.monte.media.FormatKeys.MediaType;
import org.monte.media.math.Rational;
import org.monte.media.Format;
import org.monte.screenrecorder.ScreenRecorder;

import static org.monte.media.AudioFormatKeys.*;
import static org.monte.media.FormatKeys.EncodingKey;
import static org.monte.media.FormatKeys.FrameRateKey;
import static org.monte.media.FormatKeys.KeyFrameIntervalKey;
import static org.monte.media.FormatKeys.MIME_QUICKTIME;
import static org.monte.media.FormatKeys.MediaTypeKey;
import static org.monte.media.FormatKeys.MimeTypeKey;

import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.guardian.PageObjects.LandingPage;
import com.guardian.utilities.XMLDataReader;
import com.guardian.utilities.myUtil;


public class PreBDDTests {
	
	public WebDriver driver;
	private XMLDataReader xmlDataReader;
	LandingPage landingPage;

	
	ScreenRecorder screenRecorder;
		

	@org.junit.Before
	public void SetUp() throws IOException, AWTException{

//		driver.manage().deleteAllCookies();
	
		GraphicsConfiguration gc = GraphicsEnvironment
				.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration();
	
		screenRecorder = new ScreenRecorder(gc, new Format(MediaTypeKey,MediaType.FILE, MimeTypeKey, MIME_QUICKTIME),
				new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey,ENCODING_QUICKTIME_JPEG, CompressorNameKey,
				ENCODING_QUICKTIME_JPEG, DepthKey, (int) 24,FrameRateKey, Rational.valueOf(15), QualityKey, 1.0f,
				KeyFrameIntervalKey, (int) (15 * 60)),
				new Format(MediaTypeKey,MediaType.VIDEO/*MediaType.<span class="searchterm5">VIDEO</span>*/, EncodingKey, "black", FrameRateKey,Rational.valueOf(30)),
				null);

		
		
			xmlDataReader=new XMLDataReader();
			
			switch (myUtil.browserName) {
			case "FireFox":
				driver = new FirefoxDriver();
				
				break;
				
			case "Headless":
				 driver = new HtmlUnitDriver();
				
				break;	
				
			case "Chrome":
				
				
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.home")+"/Documents/Selenium/ChromeDriver/ChromeDriver");
				driver=new ChromeDriver();
				
				break;
				
			default :
				driver = new FirefoxDriver();
				
				break;
		}
			driver.get(myUtil.BASE_URL);
			driver.manage().window().maximize();
			
		}//end Before

	
	
	
	
	/* Working Junit tests */
	@org.junit.Test
	public void ValidLogInTest() throws Exception{
		landingPage = new LandingPage(driver);
		String uname=xmlDataReader.searchElement("validUser", "email");
		String password=xmlDataReader.searchElement("validUser", "password");
		String expectedUserName=xmlDataReader.searchElement("validUser", "username");
		
		screenRecorder.start();
		Thread.sleep(4000);
		JavascriptExecutor jsx = (JavascriptExecutor) driver;
		//jsx.executeScript("document.write(Date());");
		jsx.executeScript("alert('*****HIIIIIII***********')");
		
		Thread.sleep(4000);
		driver.switchTo().alert().accept();
		landingPage.OpenLoginInterface();

		
		landingPage.EnterUsername(uname);
		landingPage.EnterPassword(password);
		landingPage.Submit();
		String LoggedInUserName=landingPage.LoginSuccessCheck(expectedUserName);
		System.out.println("\n**Valid user login Test; Result="+LoggedInUserName);	
		landingPage.LogOutAction();
		screenRecorder.stop();


	}
	
	@org.junit.Test
	public void InValidLogInTest() throws Exception{
		
		
		landingPage = new LandingPage(driver);
		String uname=xmlDataReader.searchElement("invalidUser", "email");
		String password=xmlDataReader.searchElement("invalidUser", "password");
		String expectedUserName=xmlDataReader.searchElement("invalidUser", "username");
		
		screenRecorder.start();
		
		JavascriptExecutor jsx = (JavascriptExecutor) driver;
		//jsx.executeScript("document.write(Date());");
		jsx.executeScript("alert('*****HIIIIIII***********')");
		
		Thread.sleep(4000);
		driver.switchTo().alert().accept();
		
		landingPage.OpenLoginInterface();
		landingPage.EnterUsername(uname);
		landingPage.EnterPassword(password);
		landingPage.Submit();
		landingPage.InvaliLoginCheck();
	
		screenRecorder.stop();
		
		

	}
	
//	@org.junit.Test
//	public void ValidInvalidLoginCheck() throws Exception{
//		
//		
//		landingPage = new LandingPage(driver);
//		String uname=xmlDataReader.searchElement("invalidUser", "email");
//		String password=xmlDataReader.searchElement("invalidUser", "password");
//		String expectedUserName=xmlDataReader.searchElement("invalidUser", "username");
//		
//		screenRecorder.start();
//		landingPage.OpenLoginInterface();
//		landingPage.EnterUsername(uname);
//		landingPage.EnterPassword(password);
//		landingPage.Submit();
//		assertEquals("\n**Checking returned Node", expectedUserName, landingPage.VerifyLogin(uname));
//		
//		driver.manage().deleteAllCookies();
//		
//		driver.navigate().refresh();
//	
//		screenRecorder.stop();
//		
//		
//
//	}

	@After
	public void finish() throws IOException {
		
		driver.close();
		driver.quit();
		
		
	
	}
	

}
