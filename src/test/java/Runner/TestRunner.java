package Runner;

import base.BaseTest;
import cucumber.api.CucumberOptions;
import cucumber.api.testng.CucumberFeatureWrapper;
import org.testng.annotations.Test;

//@RunWith(Cucumber.class)
@CucumberOptions(
        features ="Features",
		glue={"seleniumgluecode"},
		tags = {"~@Ignore"},
		plugin = {
				"pretty",
				"html:target/cucumber-reports/cucumber-pretty",
				"json:target/cucumber-reports/CucumberTestReport.json",
				"rerun:target/cucumber-reports/rerun.txt",
				"junit:target/cucumber-reports/cucumber.xml"
		})

public class TestRunner extends BaseTest {



	@Test(groups = "cucumber", description = "Runs Cucumber Feature", dataProvider = "features")
	public void feature(CucumberFeatureWrapper cucumberFeature) {
		testNGCucumberRunner.runCucumber(cucumberFeature.getCucumberFeature());
	}


}

