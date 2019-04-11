package base;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.lang.model.element.Element;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.UnhandledException;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.relevantcodes.extentreports.LogStatus;

import utils.Common;



public class BasePageSelenium extends BaseTest {
	
	protected static Logger LOGGER = LoggerFactory.getLogger(BasePageSelenium.class);
	
	protected By ReferenceElement = null;
	
	public void VerifyReferenceElement(String StepName)
	{
		if(ReferenceElement != null)
		{
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(ReferenceElement));
			webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(ReferenceElement));
			LOGGER.debug(StepName + "La page est bien affichée");
			ExtentTestManager.getTest().log(LogStatus.INFO, "<b style=\"font-size: 110%\">" + StringEscapeUtils.escapeHtml(StepName) + "</b>", StringEscapeUtils.escapeHtml("La page est bien affichée"));
		}
		else
			LOGGER.debug(StringEscapeUtils.escapeHtml("L'élément de référence de la page n'est pas défini"));
	}
	
	public void VerifyErrors()
	{
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		List<WebElement> elements = driver.findElements(By.xpath("//*[contains(@class,'icon-alert')]"));
		for (WebElement webElement : elements) 
		{
			WebElement parent = webElement.findElement(By.xpath(".."));
			String message = parent.getAttribute("id") != null && parent.getAttribute("id").length() > 0 ? StringEscapeUtils.escapeHtml("HTML id: " + parent.getAttribute("id")) + "<br>" : "";
			//ExtentTestManager.getTest().log(LogStatus.FATAL, "Erreur : ", message + " Message visible: " + parent.getText());
			
			String base64Screenshot="";
			if (webDriver != null) {
		        base64Screenshot = "data:image/png;base64,"+((TakesScreenshot)webDriver).
		                getScreenshotAs(OutputType.BASE64);				
			}
			if (appiumDriver != null) {
		        base64Screenshot = "data:image/png;base64,"+((TakesScreenshot)appiumDriver).
		                getScreenshotAs(OutputType.BASE64);					
			}
			message += "Message: " + StringEscapeUtils.escapeHtml(parent.getText());
			ExtentTestManager.getTest().log(LogStatus.FAIL, "Message d'erreur sur le site", message + ExtentTestManager.getTest().addBase64ScreenShot(base64Screenshot));
		}

		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	}

	public static String getBrowserName() {
	    Capabilities cap = ((RemoteWebDriver) webDriver).getCapabilities();
	    String browserName = cap.getBrowserName().toLowerCase();
	    //String os = cap.getPlatform().toString();
	    //String v = cap.getVersion().toString();
		return browserName;
	}
	
    public static void waitForLoad(WebDriver driver) {
    	Common.sleep(500);
        ExpectedCondition<Boolean> pageLoadCondition = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
                    }
                };
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(pageLoadCondition);
    }	
	
	public static String openUrl(String url) {
		LOGGER.info("BrowserName="+getBrowserName());
		webDriver.get(url);
		return webDriver.getTitle();
	}
	
	public static void waitAndClick(By by) {
		LOGGER.debug("waitAndClick : " + by.toString());
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(by));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(by));
		webDriverWait.until(ExpectedConditions.elementToBeClickable(by));
		webDriver.findElement(by).click();
	}
	
	public static void waitAjaxAndClick(By by) {
		webDriverEvent.findElement(by); // Synchro ajax beforeFindBy  SeleniumEventListener.java
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(by));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(by));
		webDriverWait.until(ExpectedConditions.elementToBeClickable(by));
		webDriver.findElement(by).click();
	}
	
	public static void waitSendText(By by, String text) {
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(by));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(by));
		webDriver.findElement(by).sendKeys(text);
	}		
	
	public static void waitSendTextPressEnter(By by, String text) {
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(by));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(by));
		webDriver.findElement(by).sendKeys(text);
		webDriver.findElement(by).sendKeys(Keys.RETURN);		
		if (getBrowserName().equals("firefox")) { 
			waitForLoad(webDriver);
		} 
	}	
	
	public static void waitAndSelect(By by, String Text) {
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(by));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(by));
		webDriverWait.until(ExpectedConditions.elementToBeClickable(by));
		WebElement element = webDriver.findElement(by);
		Select dropdown = new Select(element);
		dropdown.selectByVisibleText(Text);
	}	
	
	public static void waitAndSync(By by) {
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(by));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(by));
	}
	
	public static void CheckAttribute(By by, String sNameAttribute, String sValue ,String sSTEPNAME) 
	{
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(by));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(by));
		String sActualValueAttibute = webDriver.findElement(by).getAttribute(sNameAttribute);
		LogStatus FinalStatus = LogStatus.UNKNOWN;
		if (webDriver.findElement(by).getAttribute(sNameAttribute).equals(sValue)) 
			FinalStatus = LogStatus.PASS;
		else
			FinalStatus = LogStatus.FAIL;
		
		LOGGER.debug("la valeur du champ : " + webDriver.findElement(by).getAttribute("id") + " valeur attendue : " + sValue + " valeur actuelle : " + sActualValueAttibute);
		ExtentTestManager.getTest().log(FinalStatus, StringEscapeUtils.escapeHtml(sSTEPNAME), "La valeur attendue : \"" + StringEscapeUtils.escapeHtml(sValue) + "\"<br>" + "La valeur obtenue : \"" + StringEscapeUtils.escapeHtml(sActualValueAttibute) + "\"");
	}
	
	public static void CheckSelectValues(By by, String SemicolonSeparatedValues, String StepName) 
	{
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(by));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(by));
		WebElement element = driver.findElement(by);
		
		List<WebElement> elements = element.findElements(By.xpath("./option"));
		String SelectValues = "";
		for (WebElement webElement : elements)
		{
			if(SelectValues == "")
				SelectValues += webElement.getText();
			else
				SelectValues += ";" + webElement.getText();
		}
		
		LogStatus FinalStatus;
		if(SemicolonSeparatedValues.equals(SelectValues))
			FinalStatus = LogStatus.PASS;
		else
			FinalStatus = LogStatus.FAIL;

		ExtentTestManager.getTest().log(FinalStatus, StringEscapeUtils.escapeHtml(StepName), "Les valeurs attendues : \"" + StringEscapeUtils.escapeHtml(SemicolonSeparatedValues) + "\"<br>Les valeurs obtenues : \"" + StringEscapeUtils.escapeHtml(SelectValues) + "\"");
	}
	
	public static void CheckText(By by, String sValue, String sSTEPNAME) 
	{
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(by));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(by));
		String sActualValueAttibute = webDriver.findElement(by).getText();
		
		LogStatus FinalStatus;
		if(webDriver.findElement(by).getText().equals(sValue))
			FinalStatus = LogStatus.PASS;
		else
			FinalStatus = LogStatus.FAIL;

		LOGGER.debug("la valeur du champ : " + webDriver.findElement(by).getAttribute("id") + " valeur attendue : " + sValue + " valeur actuelle : " + sActualValueAttibute);
		ExtentTestManager.getTest().log(FinalStatus, StringEscapeUtils.escapeHtml(sSTEPNAME), "La valeur attendue : \"" + StringEscapeUtils.escapeHtml(sValue) + "\"<br>" + "La valeur obtenue : \"" + StringEscapeUtils.escapeHtml(sActualValueAttibute) + "\"");
	}
	
	public static void sleep(long Milliseconds) {
		try 
		{
			Thread.sleep(Milliseconds);
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
