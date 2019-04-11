package Runner;

import java.io.File;
import base.BaseTest;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.testng.annotations.Test;

import com.cucumber.listener.ExtentCucumberFormatter;
import com.cucumber.listener.Reporter;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
        features ="Features"
        ,glue={"seleniumgluecode"}
                )

public class TestRunner extends BaseTest {
	@Test(groups = { "SELENIUM_LOCAL_CHROME" })
	public void Test() throws Exception
	{
		System.out.println("test");
	}
	
}