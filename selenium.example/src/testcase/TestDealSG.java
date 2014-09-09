package testcase;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import utils.UtilFunction;
import data.driven.DataDrivenClass;

public class TestDealSG {
	UtilFunction utilFunction;
	DataDrivenClass dataDriven;
	WebDriver wd;
	WebElement element;
	WebDriverWait wait;
	final static String url = "http://www.deal.com.sg/";
	String parentHandle;
	final static String CSV_MASTER_FILE = "CSV/MasterData_Template.csv";
	int MASTER_LINE_NO = 1;

	@BeforeClass
	public void setUp() {
		wd = new FirefoxDriver();
		wd.get(url);
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
	public void testLoginToSite() {
		String[] text = dataDriven.readMasterCsvFile(CSV_MASTER_FILE,
				MASTER_LINE_NO);
		try {
			utilFunction = new UtilFunction();
			utilFunction.login(wd, text[0]);
		} catch (WebDriverException we) {
			System.out.print("Fail to login!");
			UtilFunction.takeScreenShot();
			we.printStackTrace();
		}

		element = wd.findElement(By
				.xpath("//a[contains(@class,'term-products')]"));
		element.click();
		wd.findElement(
				By.xpath("//div[contains(@class, 'deal-tid-home_delivery')]"))
				.click();
		wd.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		WebElement element2 = wd.findElement(By.id("deal-main-pic-wrapper"));
		for (int i = 0; i < 12; i++) {
			utilFunction.scrollDown(String.valueOf(element2.getLocation()
					.getY()));
		}
		System.out.println(String.valueOf(element2.getLocation().getY()));
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (element2.isDisplayed()) {
			System.out.println("Appear!");
			element2 = wd
					.findElement(By
							.xpath("//img[contains(@class, 'lazy imagecache imagecache-product_deal_front_wide_2 imagecache-default imagecache-product_deal_front_wide_2_default')]"));
			utilFunction.switchToNewWindow(element2);
		} else {
			System.out.println("Not appear!");
		}
		wd.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		element = wd.findElement(By
				.xpath("//span[contains(@class, 'currency-number')]"));
		Assert.assertEquals(element.getText(), "$6.00");
		// switchBackToOldWindow();
	}

}
