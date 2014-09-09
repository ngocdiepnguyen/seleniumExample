package testcase;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import utils.UtilFunction;
import data.driven.DataDrivenClass;

public class TestPrevNextImageArrow {
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
	}

	@AfterClass
	public void tearDown() {
		if (wd != null) {
			wd.quit();
		}
	}

	@Test
	public void testClickImageArrow() {
		utilFunction = new UtilFunction();
		try {
			String[] text = dataDriven.readMasterCsvFile(CSV_MASTER_FILE,
					MASTER_LINE_NO);
			utilFunction.login(wd, text[0]);
			element = wd.findElement(By
					.xpath("//a[contains(@href,'/travel/singapore')]"));
			element.click();
			wd.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			element = wd
					.findElement(By
							.xpath("//div[contains(@class,'jcarousel-prev jcarousel-prev-horizontal')]"));
			String soucrePrev = wd.getPageSource();
			for (int i = 0; i < 5; i++) {
				utilFunction.clickAnElement(wd, element);
			}
			String sourceAfter = wd.getPageSource();
			if (soucrePrev.equals(sourceAfter)) {
				System.out.println("No change after click the arrow.");
			} else {
				System.out.println("Changed after click the arrow.");
			}
		} catch (WebDriverException we) {
			UtilFunction.takeScreenShot();
			we.printStackTrace();
		}
	}
}
