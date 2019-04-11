#!/bin/bash

startAppium(){
	if [ "$(uname)" == "Darwin" ]; then
		startAppiumOSX
	elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
		startAppiumLinux
	else
		echo "Unknown OS system, exiting..."
		exit 1
	fi
}

startAppiumOSX(){
	sudo chown -R _usbmuxd:_usbmuxd /var/db/lockdown
	sudo chmod -R 777 /var/db/lockdown
	if [ -z ${UDID} ] ; then
		export UDID=${IOS_UDID}
	fi
		echo "UDID is ${UDID}"
	# Create the screenshots directory, if it doesn't exist'
	mkdir -p .screenshots
        echo "Starting Appium on Mac..."
        export AUTOMATION_NAME="XCUITest"
	appium-1.10 -U ${UDID} -p ${appiumPort} --webdriveragent-port ${webdriveragentPort} --log-no-colors --log-timestamp --tmp /tmp/${IOS_UDID}/
}

startAppiumLinux(){
	# Create the screenshots directory, if it doesn't exist'
	mkdir -p .screenshots
        echo "Starting Appium on Linux..."
        set AUTOMATION_NAME=UiAutomator2
	appium --log-no-colors --log-timestamp
}

executeTests(){
	echo "Extracting tests.zip..."
	unzip tests.zip
	if [ "$(uname)" == "Darwin" ]; then
	   	echo "Running iOS Tests..."
		mvn clean test -DexecutionType=serverside -Denv=SUT_DEFAULT -DsuiteXmlFile=src/test/resources/testSuite/IOS_Demo.xml
	elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
	    echo "Running Android Tests..."
		mvn clean test -DexecutionType=serverside -Denv=SUT_DEFAULT -DsuiteXmlFile=src/test/resources/testSuite/Android_Demo.xml
	fi
	echo "Finished Running Tests!"
	#cp target/surefire-reports/junitreports/TEST-*.xml TEST-all.xml
	cp target/surefire-reports/TEST-TestSuite.xml TEST-all.xml
	cp target/ExtentReports/ExtentReportResults.html ExtentReportResults.html
	cp target/screenshots/*.png screenshots/
	echo "Finished copy result!"
	
}

startAppium
executeTests
