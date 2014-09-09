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

public class TestResizeScreen {
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
	public void testResizeScreen() {
		utilFunction = new UtilFunction();
		try {
			String beforeSize = String.valueOf(wd.manage().window().getSize());
			UtilFunction.println("Resolution screen before: " + beforeSize);
			element = wd.findElement(By.id("lightbox2-overlay"));
			String elementBefSize = String.valueOf(element.getSize());
			UtilFunction.println("Black layer size before: "
					+ element.getSize());

			wd.manage().window().setSize(new Dimension(1200, 1024));
			String afterSize = String.valueOf(wd.manage().window().getSize());
			String elementAftSize = String.valueOf(element.getSize());
			UtilFunction.println("Resolution screen after: " + afterSize);
			UtilFunction
					.println("Black layer size after: " + element.getSize());
			Assert.assertNotEquals(elementBefSize, elementAftSize, "Assert element size");
		} catch (Exception e) {
			e.printStackTrace();
			UtilFunction.takeScreenShot();
		}
	}
}
