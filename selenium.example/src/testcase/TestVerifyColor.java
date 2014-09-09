package testcase;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
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

public class TestVerifyColor {
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
	public void testVerifyColor() {
		String[] text = dataDriven.readMasterCsvFile(CSV_MASTER_FILE,
				MASTER_LINE_NO);
		try {
			// Login part
			utilFunction = new UtilFunction();
			utilFunction.login(wd, text[0]);
			wd.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			// Click D-Luxe button
			element = wd.findElement(By
					.xpath("//a[contains(@href,'/luxury/singapore')]"));
			element.click();
			System.out.println(wd.getCurrentUrl());
			wd.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			element=wd.findElement(By.xpath("//li[contains(@class,'beecrazy-luxury last active')]"));
			UtilFunction.println("Ready to assert color..");
			String convertedColor=UtilFunction.verifyForeGroundColor(element);
			Assert.assertEquals(StringUtils.upperCase(convertedColor), "#3E3D40");
			wd.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
			UtilFunction.takeScreenShot();
		}
	}
}
