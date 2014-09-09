package testcase;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import utils.UtilFunction;
import data.driven.DataDrivenClass;

public class TestHandleTab {
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
	public void clickAndHandleTab() {
		String[] text = dataDriven.readMasterCsvFile(CSV_MASTER_FILE,
				MASTER_LINE_NO);
		try {
			utilFunction = new UtilFunction();
			UtilFunction.println("Ready to get data!","Preparing to login","Loading...");
			utilFunction.login(wd, text[0]);
			element = wd.findElement(By.id("duriana_img_1"));
			if (element.isDisplayed()) {
				System.out.println("Duriana is displayed!");
			} else {
				System.out.println("Duriana is not displayed!");
			}
			utilFunction.switchToNewWindow(element);
			wd.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			Actions moveMouse = new Actions(wd);
			// element = wd.findElement(By
			// .xpath("//img[contains(@alt, 'Black lace skirt')]"));
			element = wd
					.findElement(By
							.xpath("//ul[contains(@class,'list-unstyled cards-list cards-product')]"));
			List<WebElement> elements = element.findElements(By
					.xpath("//li[contains(@class,'card-item')]"));
			for (WebElement e : elements) {
				moveMouse.moveToElement(e).build().perform();
				Thread.sleep(5000);
			}
		} catch (WebDriverException we) {
			UtilFunction.takeScreenShot();
			we.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
