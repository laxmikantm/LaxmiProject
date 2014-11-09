//Author: Laxmi Somni
package com.guardian.PageObjects;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.guardian.utilities.*;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LandingPage extends Abstractpage {
	
	WebDriverWait myWait_lx;
	
	@FindBy(css="span.id-sign-in-top-nav.initially-off > a")
	WebElement SignInbyCSS;
	
	@FindBy(css=".identity-overlay iframe")
	WebElement LoginIframeByCSS;
	
	@FindBy(partialLinkText="Sign")
	WebElement SignInbyPartialLinkText;	
	
	@FindBy(id="email")
	WebElement userNamebyID;
	
	@FindBy(id="password")
	WebElement passwordbyID;
	
	
	@FindBy(xpath=".//*[@id='sign-in']/fieldset/div[4]/input")
	WebElement SignInByXpath;
	
//	@FindBy(xpath=".//*[@id='header']/div[1]/div[1]/div[4]/div")
//	WebElement LoggedInUsrByXpath;
	
	@FindBy(xpath=".//*[@id='header']/div[1]/div[1]/div[4]/div/ul/li[5]/a")
	WebElement SignOutByXpath;
	
	@FindBy(xpath=".//*[@id='header']/div[1]/div[1]/div[4]/div")
	WebElement LoggedInUserByXpath;

	
	
	
	
	public LandingPage(WebDriver driver){
		
		this.driver=driver;
		AjaxElementLocatorFactory aFactory= new AjaxElementLocatorFactory(driver, 100);
		PageFactory.initElements(aFactory, this);
	}
	
	
	public void OpenLoginInterface() throws InterruptedException, IOException{
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		SignInbyCSS.click();
		driver.switchTo().defaultContent();
		driver.switchTo().frame(driver.findElement(By.cssSelector(".identity-overlay iframe")));
		myUtil.takeScreenSnapShot(driver, "PageLoading");
		//myUtil.takeFieldSnapshot(driver, (driver.findElement(By.cssSelector(".identity-overlay iframe"))), "LoginFrame-");
		
		
	}
	
	
	public void EnterUsername(String uname) throws InterruptedException{
		myWait_lx = new WebDriverWait(driver, 45);
		myWait_lx.until(ExpectedConditions.elementToBeClickable(userNamebyID));
		userNamebyID.clear();
		userNamebyID.sendKeys(uname);
		
	}

	public void EnterPassword(String password){
		myWait_lx = new WebDriverWait(driver, 45);
		myWait_lx.until(ExpectedConditions.elementToBeClickable(passwordbyID));
		passwordbyID.clear();
		passwordbyID.sendKeys(password);
	}
	
	public void Submit() throws IOException{
		myUtil.takeScreenSnapShot(driver, "BeforeLogin");
		SignInByXpath.click();	
	}

	

public String LoginSuccessCheck(String expectedUserName) throws IOException{
	
	myUtil.takeScreenSnapShot(driver, "AfterLogin");
	myUtil.takeFieldSnapshot(driver, LoggedInUserByXpath, "Logged-in-user-"+LoggedInUserByXpath.getText());
	
	if(LoggedInUserByXpath.getText().contains(expectedUserName)){
		System.out.println("\n**Valid User Flow: Login successful with user name= "+LoggedInUserByXpath.getText());
		return LoggedInUserByXpath.getText();
	}	
	else
	{
		System.out.println("\n**Some issue occured. Login not Unsuccessful.");
		return "Error: Login not Successful";
	}
		
}


public boolean ValidLoginCheck(String expectedUserName) throws IOException{
	
	myUtil.takeScreenSnapShot(driver, "AfterLogin");
	myUtil.takeFieldSnapshot(driver, LoggedInUserByXpath, "Logged-in-user-"+LoggedInUserByXpath.getText());
	
	if(LoggedInUserByXpath.getText().contains(expectedUserName)){
		System.out.println("\n**Valid User Flow: Login successful with user name= "+LoggedInUserByXpath.getText());
		return true;
	}	
	else
	{
		System.out.println("\n**Login not Unsuccessful.");
		return false;
	}
		
}

public boolean InvaliLoginCheck() throws IOException{
	if((driver.findElement(By.cssSelector("body")).getText().contains(myUtil.LOGIN_FAILURE_TEXT))||driver.getTitle().contains(myUtil.SIGN_IN_SCREEN_TITLE)){
		System.out.println("\n***As expected: System has provided following validation message: '"+myUtil.LOGIN_FAILURE_TEXT+"'.");
		myUtil.takeFieldSnapshot(driver, driver.findElement(By.xpath(".//*[@id='sign-in']/fieldset/p")), "Invalid-Login-message");
		return true;
	}
	return false;
}

public void LogOutAction() throws IOException{
		Actions action= new Actions(driver);
		action.moveToElement(LoggedInUserByXpath).perform();
		
		myUtil.takeScreenSnapShot(driver, "DuringLogout-");
		
		action.moveToElement(SignOutByXpath);
		SignOutByXpath.click();

}



//Alternate Option: Single checker for Cucumber driven
public String VerifyLogin(String expectedUserName) throws IOException{
	
	myUtil.takeScreenSnapShot(driver, "AfterLogin");
	//myUtil.takeFieldSnapshot(driver, LoggedInUserByXpath, "Logged-in-user-"+LoggedInUserByXpath.getText());
	String result="";
	
	if(LoggedInUserByXpath.isDisplayed()){
		try{
			if(LoggedInUserByXpath.getText().contains(expectedUserName)){
				System.out.println("\n**Valid User flow: Login successful with user name= "+LoggedInUserByXpath.getText());
				result= LoggedInUserByXpath.getText();
			}	
			else
			{
				System.out.println("\n**Some issue occured. Login not Unsuccessful.");
				result= "Error: Login not Successful";
			}
		}	
		catch(Exception e){
			System.out.println("Exception message= "+e.getMessage());
		}
	}
	else{
		if((driver.findElement(By.cssSelector("body")).getText().contains(myUtil.LOGIN_FAILURE_TEXT))||driver.getTitle().contains(myUtil.SIGN_IN_SCREEN_TITLE)){
			System.out.println("\n***Invalid user flow:As expected: System has provided following validation message:"+myUtil.LOGIN_FAILURE_TEXT);
			myUtil.takeFieldSnapshot(driver, driver.findElement(By.xpath(".//*[@id='sign-in']/fieldset/p")), "Invalid-Login-message");
		}
	}
	
	return result;
		
}

}
	

