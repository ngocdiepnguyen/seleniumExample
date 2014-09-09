package testcase;

import java.util.List;
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

public class TestChooseOptionsInLuxuryArea {
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
	public void testChooseOptions() {
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
			// Click brand select box
			element = wd.findElement(By.id("edit-brand-filterSelectBoxIt"));
			element.click();
			WebElement dropDowns = wd.findElement(By
					.id("edit-brand-filterSelectBoxItOptions"));
			// Get all the options of brand select box
			List<WebElement> options = dropDowns.findElements(By.tagName("li"));
			// Choose an option
			utilFunction.clickAnElement(wd, options.get(7));
			wd.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			// Click price select box
			element = wd.findElement(By.id("edit-price-filterSelectBoxIt"));
			element.click();
			dropDowns = wd.findElement(By
					.id("edit-price-filterSelectBoxItOptions"));
			// Get all the options of price select box
			options = dropDowns.findElements(By.tagName("li"));
			// Choose an option
			utilFunction.clickAnElement(wd, options.get(3));
			wd.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		} catch (WebDriverException we) {
			UtilFunction.takeScreenShot();
			we.printStackTrace();
		}
	}
}
