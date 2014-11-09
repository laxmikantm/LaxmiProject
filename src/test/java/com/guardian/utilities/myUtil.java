
package com.guardian.utilities;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.monte.screenrecorder.ScreenRecorder;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;





public class myUtil {
	
	public static final String BASE_URL="http://www.theguardian.com/uk";
	public static final int WAIT_TIME = 30; 
	public static final String LOGIN_FAILURE_TEXT="Incorrect password or email address. Please try again.";
	public static final String SIGN_IN_SCREEN_TITLE="Sign in to the Guardian";
    public static final String DATA_FILE_NAME = "TestData.xls"; // File Path

    /*MultiBrowser set-up*/
    
    /* Below we can choose which browser to run the tests on*/
    /* Just set the browserName to the desired browser as necessary */
    /* note: For ChromeDriver: set following property:System.setProperty("webdriver.chrome.driver", "/Users/<username>/Documents/Selenium/ChromeDriver/ChromeDriver");
    /* To make it more advance we can use Enum */
    
    
    //public static final String browserName ="Chrome";
    public static final String browserName ="FireFox";

	
////////Reusable Snapshot Method Code////////////////////////////
	
	public static void TakeSnapShot(WebDriver driver,String prefix) throws IOException{
	
			String fileName="";
			
			fileName += prefix;
			

			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy-hhmm");
			Date date = new Date();
			
			fileName +=dateFormat.format(date);	
			
			File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			try{
			FileUtils.copyFile(srcFile, new File("target/snaps/"+fileName+".jpg"));
			}
			
			catch(NoSuchFileException e)
			{
				System.out.println("Unable to access folder for Snapshots");
				
				
			}
			
			return;
			
	}	
	
	
public static void takeScreenSnapShot(WebDriver driver,String prefix) throws IOException{
		
		String fileName="";
		
		fileName += prefix;
		
		
		DateFormat dateFormat = new SimpleDateFormat("-dd-MM-yy-hhmmss");
		Date date = new Date();
		
		fileName +=dateFormat.format(date);	
		
		File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try{
		FileUtils.copyFile(srcFile, new File("target/snapshots/"+fileName+".jpg"));
		}
		
		catch(NoSuchFileException e)
		{
			System.out.println("Unable to access folder for Snapshots");
			
			
		}
		
		return;
	}	

public static void takeFieldSnapshot(WebDriver driver,WebElement element, String prefix) throws IOException  {

	String fileName="";
	fileName += prefix;
	

	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy-hhmmss");
	Date date = new Date();
	
	fileName +=dateFormat.format(date);	
	
	File screen = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);

    Point p = element.getLocation();

    int width = element.getSize().getWidth();
    int height = element.getSize().getHeight();

    BufferedImage img = null;

    img = ImageIO.read(screen);

    BufferedImage dest = img.getSubimage(p.getX(), p.getY(), width,   
                             height);

    ImageIO.write(dest, "png", screen);

    File f = null;
	FileUtils.copyFile(screen, new File("target/snapshots/elementSpecific/"+fileName+".jpg"));

}

private ScreenRecorder screenRecorder;

public void startRecording() throws Exception
	{       
		GraphicsConfiguration gc = GraphicsEnvironment
		              .getLocalGraphicsEnvironment()
		              .getDefaultScreenDevice()
		              .getDefaultConfiguration();
	
		this.screenRecorder = new ScreenRecorder(gc);
		this.screenRecorder.start();    
	}

public void stopRecording() throws Exception
	{
		this.screenRecorder.stop();

	}

}
	
	
	


