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
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import utils.UtilFunction;
import data.driven.DataDrivenClass;

public class TestFeatureInBuyingAreaLine1 {
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
	public void testBuyingItem() {
		utilFunction = new UtilFunction();
		try {
			String[] text = dataDriven.readMasterCsvFile(CSV_MASTER_FILE,
					MASTER_LINE_NO);
			utilFunction.login(wd, text[0]);
			List<WebElement> elements = wd.findElements(By
					.className("node-add-to-cart"));
			UtilFunction.println(String.valueOf(elements.size()));
			parentHandle = wd.getWindowHandle();
			for (WebElement e : elements) {
				utilFunction.scrollToElement(e);
				e.click();
				UtilFunction.println("Ready to buy item "
						+ e.getAttribute("href"));
				buyProcess(e);
			}
		} catch (WebDriverException we) {
			we.printStackTrace();
			UtilFunction.takeScreenShot();
		}
	}

	public void buyProcess(WebElement elementToBuy) {
		utilFunction.switchToNewWindow(elementToBuy);
		List<WebElement> elements = wd
				.findElements(By.className("deal-button"));
		for (WebElement e : elements) {
			UtilFunction.println(e.getTagName());
		}
		String tag;
		if (elements.size() == 2) {
			tag = elements.get(1).getTagName();
		} else {
			tag = elements.get(0).getTagName();
		}
		UtilFunction.println(tag);
		MASTER_LINE_NO = 2;
		String[] text = dataDriven.readMasterCsvFile(CSV_MASTER_FILE,
				MASTER_LINE_NO);
		if ("span".equals(tag)) {
			element = wd
					.findElement(By
							.xpath("//input[contains(@class,'form-submit node-add-to-cart')]"));
			UtilFunction.println(element.getText());
			utilFunction.clickAnElement(wd, element);

		} else {
			element = wd.findElement(By
					.className("node-add-to-cart-redemption"));
			UtilFunction.println(element.getText());
			utilFunction.clickAnElement(wd, element);
			element = wd.findElement(By.id("edit-options-1-qty"));
			utilFunction.selectOptionInSelectBox(element, text[1]);
			element = wd.findElement(By.id("edit-submit"));
			utilFunction.clickAnElement(wd, element);
			wd.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		}

		utilFunction.scrollDown("400");
		UtilFunction.println("Scrolling finished!");

		wd.findElement(By.id("edit-name")).sendKeys(text[2]);
		wd.findElement(By.id("edit-pass")).sendKeys(text[3]);
		wd.findElement(By.id("edit-panes-customer-primary-email")).sendKeys(
				text[4]);

		element = wd.findElement(By
				.id("edit-panes-billing-billing-ucxf-salutation-title"));
		utilFunction.selectOptionInSelectBox(element, text[5]);

		wd.findElement(By.id("edit-panes-billing-billing-first-name"))
				.sendKeys(text[6]);
		wd.findElement(By.id("edit-panes-billing-billing-last-name")).sendKeys(
				text[7]);
		wd.findElement(By.id("edit-panes-billing-billing-company")).sendKeys(
				text[8]);
		wd.findElement(By.id("edit-panes-billing-billing-street1")).sendKeys(
				text[9]);
		wd.findElement(By.id("edit-panes-billing-billing-street2")).sendKeys(
				text[10]);

		utilFunction.scrollDown("300");

		wd.findElement(By.id("edit-panes-billing-billing-city")).sendKeys(
				text[11]);
		wd.findElement(By.id("edit-panes-billing-billing-postal-code"))
				.sendKeys(text[12]);

		element = wd.findElement(By.id("edit-panes-billing-billing-country"));
		utilFunction.selectOptionInSelectBox(element, text[13]);

		wd.findElement(By.id("edit-panes-billing-billing-phone")).sendKeys(
				text[14]);

		if (text[15].equals("paypal")) {
			element = wd.findElement(By
					.id("edit-panes-payment-payment-method-paypal-wps"));
			element.click();
		} else {
			element = wd.findElement(By
					.id("edit-panes-payment-payment-method-deal-cc"));
			element.click();
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UtilFunction.println("Clicking radio button finished!",
				"Preparing to click check box...");
		String result = "";
		// Consider to select default credit card check box
		element = wd.findElement(By.id("edit-cc-store"));
		if (!text[15].equals("paypal")) {
			UtilFunction.println("---CHECK BOX DEFAULT CREDIT CARD---",
					"Data input: " + text[16],
					"Preparing to execute statement...",
					"Was this check box CHECKED?" + element.isSelected(),
					"Is this check box DISPLAYED?" + element.isDisplayed(),
					"Executing...", "\t----DONE----", "\n");
			if (text[16].equals("Y")) {
				if (!element.isSelected()) {
					element.click();
				}
			} else {
				if (element.isSelected()) {
					element.click();
				}
			}
			result += "Default credit card: " + element.isSelected();
			Assert.assertFalse(element.isSelected(),
					"Assert Default Credit Card check box");
		} else {
			Assert.assertFalse(element.isDisplayed(),
					"Assert Default Credit Card check box");
			UtilFunction.println("Default credit card is not displayed.");
		}
		// Consider to select receiving email check box
		element = wd.findElement(By
				.id("edit-panes-uc-deal-newsletter-deal-newsletter-subscribe"));
		UtilFunction.println("---CHECK BOX RECEIVING EMAIL---",
				"Data input => " + text[17],
				"Preparing to execute statement...",
				"Was this check box CHECKED? => " + element.isSelected(),
				"Executing...", "\t----DONE----", "\n");
		if (text[17].equals("Y")) {
			if (!element.isSelected()) {
				element.click();
			}
		} else {
			if (element.isSelected()) {
				element.click();
			}
		}
		result += " Receiving email: " + element.isSelected();
		Assert.assertFalse(element.isSelected(),
				"Assert Receiving Email check box");
		// Consider to select agreeing policies
		element = wd.findElement(By
				.id("edit-panes-uc-ct-deal-tc-agree-deal-tc"));
		UtilFunction.println("---CHECK BOX AGREEING POLICIES---",
				"Data input => " + text[18],
				"Preparing to execute statement...",
				"Was this check box CHECKED? => " + element.isSelected(),
				"Executing...", "\t----DONE----", "\n");
		if (text[18].equals("Y")) {
			if (!element.isSelected()) {
				element.click();
			}
		} else {
			if (element.isSelected()) {
				element.click();
			}
		}
		result += " Agreeing policies: " + element.isSelected();
		Assert.assertTrue(element.isSelected(),
				"Assert Agreeing Policies Check box");
		UtilFunction.println("Clicking check boxes finished!", "RESULT:",
				"These options are selected?", result);
		utilFunction.switchBackToOldWindow(parentHandle);
	}
}
