package testcase;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import utils.UtilFunction;
import data.driven.DataDrivenClass;

public class TestImageMovedInTravelArea {
	UtilFunction utilFunction;
	DataDrivenClass dataDriven;
	WebDriver wd;
	WebElement element;
	WebDriverWait wait;
	final static String URL = "http://www.deal.com.sg/";
	final static String CSV_MASTER_FILE = "CSV/MasterData_Template.csv";
	int MASTER_LINE_NO = 1;
	String parentHandle;

	@BeforeClass
	public void setUp() {
		wd = new FirefoxDriver();
		wd.get(URL);
		wd.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wd.manage().window().setSize(new Dimension(1200, 1024));
		dataDriven = new DataDrivenClass();
		wait = new WebDriverWait(wd, 60);
	}

	@AfterClass
	public void tearDown() {
		if (wd != null) {
			wd.quit();
		}
	}

	@Test
	public void testImageMove() {
		utilFunction = new UtilFunction();
		try {
			String[] text = dataDriven.readMasterCsvFile(CSV_MASTER_FILE,
					MASTER_LINE_NO);
			utilFunction.login(wd, text[0]);
			element = wd.findElement(By
					.xpath("//a[contains(@href,'/travel/singapore')]"));
			element.click();
			wd.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			element = wd.findElement(By.id("deal-two-columns-main-pic"));
			element.click();

			element = wd.findElement(By
					.className("deal-two-columns-description-hover"));
			utilFunction.switchToNewWindow(element);
			wd.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			utilFunction.scrollDown("100");
			element = wd.findElement(By.className("clip"));
			WebElement image = element.findElement(By.tagName("img"));
			String valueClass1 = image.getAttribute("class");
			if (!valueClass1.equals("")) {
				Thread.sleep(3000);
			}
			image.click();
			Thread.sleep(500);
			String valueClass2 = image.getAttribute("class");
			Assert.assertNotSame(
					valueClass1,
					valueClass2,
					"Assert not same before and after click image to know if the active image is changed");
			if (valueClass1.equals(valueClass2)) {
				UtilFunction.println(valueClass1, valueClass2,
						"Image is not changed!");
			} else {
				UtilFunction.println("Image is changed!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			UtilFunction.takeScreenShot();
		}
	}
}
