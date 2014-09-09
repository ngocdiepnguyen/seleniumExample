package testcase;

import java.util.List;
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

public class TestIsImageLoaded {
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
	public void testIsImageLoaded() {
		utilFunction = new UtilFunction();
		try {
			String[] text = dataDriven.readMasterCsvFile(CSV_MASTER_FILE,
					MASTER_LINE_NO);
			utilFunction.login(wd, text[0]);
			boolean result = false;
			List<WebElement> imageList = wd.findElements(By.tagName("img"));
			String srcImageTarget = "http://static.deal.com.sg/sites/default/files/imagecache/product_deal_front_wide/zen-dining-main.jpg";
			// String[] srcImageTarget = new String[imageList.size()];
			// for (int i = 0; i < imageList.size(); i++) {
			// srcImageTarget[i] = imageList.get(i).getAttribute("src");
			// }
			result = UtilFunction.canImageLoadedCompletely(imageList,
					srcImageTarget);
			Assert.assertTrue(result);
			Thread.sleep(5000);
			utilFunction.scrollToElement(wd.findElement(
					By.xpath("//img[contains(@class,'imagecache imagecache-product_deal_front_wide imagecache-default imagecache-product_deal_front_wide_default')]")));
					
			Thread.sleep(5000);
		} catch (Exception e) {
			e.printStackTrace();
			UtilFunction.takeScreenShot();
		}
	}
}
