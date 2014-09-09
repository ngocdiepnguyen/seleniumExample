package testcase;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestGoogling {
	WebDriver wd;
	WebDriverWait wait;
	WebElement element;
	final static String url = "https://www.google.com.vn";

	@BeforeClass
	public void setUp() {
		wd = new FirefoxDriver();
		wd.get(url);
		wd.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@AfterClass
	public void tearDown() {
		if (wd != null) {
			wd.quit();
		}
	}

	@Test
	public void testGoogle() {
		element = wd.findElement(By.id("gbqfq"));
		element.click();
		System.out.println(element.getTagName());
		element.sendKeys("Selenium?");
		element.sendKeys(Keys.ENTER);
		wd.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
	}
}
