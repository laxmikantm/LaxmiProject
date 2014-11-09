package TestStaging;

import java.util.Iterator;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.guardian.utilities.myUtil;

public class ValidateCookieMandate {
	WebDriver driver;
	
	Set<Cookie> cookies;
	Iterator<Cookie> itr;
	
	@Before
	public void startUp(){
		driver=new FirefoxDriver();
	}
	
	@Test
	public void checkCookieMandate(){
		driver.get(myUtil.BASE_URL);

		driver.manage().window().maximize();
		driver.findElement(By.cssSelector(".hide-bar")).click();
		cookies = driver.manage().getCookies();
		itr = cookies.iterator();
	
	
	Cookie cookie= driver.manage().getCookieNamed("closed_noticebar_cookie");  
	cookie.getValue();
	System.out.println("\n** Cookie Mandate cookie = '"+cookie.getName()+"' And Its value= '"+cookie.getValue()+"' .");
	System.out.println("\n** Expiry date set after Accepting the cookie mandate: '"+cookie.getExpiry()+"' .");
	System.out.println("\n Cookie: Is it HttpOnly?"+cookie.isHttpOnly());
	
	
	}
	
	@After
	public void closeup(){
		driver.quit();
	}
}
