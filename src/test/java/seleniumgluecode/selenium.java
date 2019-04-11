package seleniumgluecode;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import org.junit.Assert;
import base.BaseTest;;

public class selenium extends BaseTest {
	
	//public static WebDriver driver;
	@Test(groups = { "SELENIUM_LOCAL_CHROME" })
    @Given("^user is  on homepage$")
    public void user_is_on_homepage() throws Throwable {     
    	//System.setProperty("webdriver.gecko.driver","C:/temp/geckodriver.exe");
    	//driver = new FirefoxDriver();
    	//driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    	LOGGER.debug("erreur");
        driver.get("http://automationpractice.com/index.php");
    }
    
    @When("^user navigates to Login Page$")
    public void user_navigates_to_Login_Page() throws Throwable {
        driver.findElement(By.linkText("Sign in")).click();
    }
    
    @When("^user enters username and Password$")
    public void user_enters_username_and_Password() throws Throwable {
    	driver.findElement(By.id("email")).sendKeys("blog.cucumber@gmail.com");
        driver.findElement(By.id("passwd")).sendKeys("Cucumber@blog");
        driver.findElement(By.id("SubmitLogin")).click();   
    }
    
    @Then("^success message is displayed$")
    public void success_message_is_displayed() throws Throwable {
    	//String exp_message = "Welcome to your account. Here you can manage all   of your personal information and orders.";
    	//Stringactual = driver.findElement(By.cssSelector(".info-account")).getText();
        //Assert.assertEquals(exp_message, actual);
        driver.quit();  
    }      
}