package testcase;

import java.util.concurrent.TimeUnit;

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

public class TestBrokenImage {
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
	public void testIsNoBrokenImage() {
		utilFunction = new UtilFunction();
		try {
			String[] text = dataDriven.readMasterCsvFile(CSV_MASTER_FILE,
					MASTER_LINE_NO);
			utilFunction.login(wd, text[0]);
			boolean result=UtilFunction.isNoBrokenImage(wd);
			Assert.assertTrue(result, "There are no broken images.");
			
		} catch (Exception e) {
			e.printStackTrace();
			UtilFunction.takeScreenShot();
		}
	}
}
