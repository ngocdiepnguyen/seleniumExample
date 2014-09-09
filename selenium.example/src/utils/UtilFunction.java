package utils;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

/**
 * 
 * @author diep.ngoc
 *
 */
public class UtilFunction {
	WebDriver wd;

	public WebDriver getWd() {
		return wd;
	}

	public void setWd(WebDriver wd) {
		this.wd = wd;
	}

	public void login(WebDriver wd, String email) {
		setWd(wd);
		WebElement element = wd.findElement(By.id("edit-email"));
		element.click();
		element.sendKeys(email);
		element = wd.findElement(By.id("edit-submit"));
		element.click();
		wd.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		element = wd.findElement(By.id("viralninjasClose"));
		if (element.isDisplayed()) {
			System.out.println("Close button appear!");
		} else {
			System.out.println("Close button doesnot appear!");
		}
		element.click();
		wd.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

	}

	public static void takeScreenShot() {
		File directory = new File(".");
		// System.out.println(directory.getCanonicalPath());
		// get current date time with Date() to create unique file name
		DateFormat dateFormat = new SimpleDateFormat("dd_MMM_yyyy__hh_mm_ssaa");
		// get current date time with Date()
		Date date = new Date();
		// System.out.println(dateFormat.format(date));
		// To identify the system
		InetAddress ownIP;
		try {
			ownIP = InetAddress.getLocalHost();
			// System.out.println("IP of my system is := "+ownIP.getHostAddress());
			String NewFileNamePath;
			try {
				NewFileNamePath = directory.getCanonicalPath() + "/ScreenShot/"
						+ dateFormat.format(date) + "_"
						+ ownIP.getHostAddress() + "_" + ".png";
				println(NewFileNamePath);
				// Capture the screen shot of the area of the screen defined by
				// the
				// rectangle
				Robot robot = new Robot();
				BufferedImage bi = robot.createScreenCapture(new Rectangle(
						1280, 1024));
				ImageIO.write(bi, "png", new File(NewFileNamePath));
			} catch (UnknownHostException | AWTException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void switchToNewWindow(WebElement element) {
		String parentHandle = wd.getWindowHandle();
		element.click();
		for (String winHandle : wd.getWindowHandles()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			wd.switchTo().window(winHandle);
		}
		wd.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	public void switchBackToOldWindow(String parentHandle) {
		wd.close();
		wd.switchTo().window(parentHandle);
	}

	public void scrollDown(String posY) {
		JavascriptExecutor script = (JavascriptExecutor) wd;
		script.executeScript("window.scrollBy(0," + posY + ")", "");
	}

	public void scrollToElement(WebElement element) {
		element.sendKeys(Keys.PAGE_DOWN);
	}

	// Not Working
	// public void scrollToElement(WebElement element) {
	//
	// ((JavascriptExecutor) wd).executeScript(
	// "arguments[0].scrollIntoView(true);", element);
	// }

	public void clickAnElement(WebDriver driver, WebElement element) {
		Actions action = new Actions(driver);
		action.moveToElement(element).click().build().perform();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void selectOptionInSelectBox(WebElement selectBoxElement,
			String optionValue) {
		Select selectBox = new Select(selectBoxElement);
		selectBox.selectByVisibleText(optionValue);
	}

	public static void println(Object... param) {
		for (int i = 0; i < param.length; i++) {
			System.out.println(param[i]);
		}
	}

	public static String verifyForeGroundColor(WebElement element) {
		String cssColor = element.getCssValue("color");
		String convertedColor = Color.fromString(cssColor).asHex();
		println("Actual foreground color of this element is "
				+ StringUtils.upperCase(convertedColor));
		return StringUtils.upperCase(convertedColor);
	}

	public static String verifyBackGroundColor(WebElement element,
			String expectedColor) {
		String cssColor = element.getCssValue("background");
		String actualColor = Color.fromString(cssColor).asHex();
		println("Actual background color of this element is "
				+ StringUtils.upperCase(actualColor));
		return StringUtils.upperCase(actualColor);
	}

	public static boolean canImageLoadedCompletely(List<WebElement> imageList,
			String srcImageTarget) {
		List<String> srcList = new ArrayList<String>();
		for (WebElement element : imageList) {
			srcList.add(element.getAttribute("src"));
		}

		for (String item : srcList) {
			if (StringUtils.isNotEmpty(item)) {
				if (item.equals(srcImageTarget)) {
					URL url;
					try {
						url = new URL(item);
						HttpURLConnection request = (HttpURLConnection) url
								.openConnection();
						request.connect();
						int responseCode = request.getResponseCode();
						if (responseCode == HttpURLConnection.HTTP_OK) {
							println("Src " + item + " is loaded!");
							return true;
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		println("Src " + srcImageTarget + " is not loaded or cannot found!");
		return false;
	}

	public static boolean canImagesLoadedCompletely(List<WebElement> imageList,
			String... srcImagesTarget) {
		List<String> srcList = new ArrayList<String>();
		List<String> unLoadedList = new ArrayList<String>();
		int count = 0;
		for (WebElement element : imageList) {
			srcList.add(element.getAttribute("src"));
		}
		println("Total actual images: " + imageList.size());
		for (String item : srcList) {
			if (StringUtils.isNotEmpty(item)) {
				for (int i = 0; i < srcImagesTarget.length; i++) {
					if (item.equals(srcImagesTarget[i])) {
						URL url;
						try {
							url = new URL(item);
							HttpURLConnection request = (HttpURLConnection) url
									.openConnection();
							int responseCode = request.getResponseCode();
							if (responseCode == HttpURLConnection.HTTP_OK) {
								println((count + 1) + "\tSrc " + item
										+ " is loaded!");
								break;

							} else {
								unLoadedList.add(item);
								println((count + 1) + "\tSrc " + item
										+ " is not loaded!");
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			count++;
		}
		if (imageList.size() > count) {
			println("There are " + count + " image source link.");
		}
		if (unLoadedList.size() != 0) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean isNoBrokenImage(WebDriver driver) {
		EventFiringWebDriver firingDriver = new EventFiringWebDriver(driver);
		List<WebElement> elements = firingDriver
				.findElements(By.tagName("img"));
		int countBrokenImages = 0;
		// Declaring a dynamic string of array which will store src of all the
		// broken images
		List<String> brokenImageUrl = new ArrayList<String>();

		String script = "return (typeof arguments[0].naturalWidth!=\"undefined\" &&  arguments[0].naturalWidth>0)";

		for (WebElement image : elements) {
			if (image.isDisplayed()) {
				Object imgStatus = firingDriver.executeScript(script, image);
				if (imgStatus.equals(false)) {
					String currentImageUrl = image.getAttribute("src");
					String imageUrl = currentImageUrl;
					brokenImageUrl.add(imageUrl);
					countBrokenImages++;
				}
			}
		}

		// Printing the src of the broken images if any
		System.out.println("Number of broken images found in the page : "
				+ countBrokenImages);
		if (brokenImageUrl.size() != 0) {
			for (String z : brokenImageUrl) {
				System.out.println(z);
			}
			return false;
		}
		return true;

	}
}