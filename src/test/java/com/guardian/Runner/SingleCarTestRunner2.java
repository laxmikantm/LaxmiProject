package com.guardian.Runner;

import org.junit.runner.RunWith;

import com.guardian.Reporting.MyReporter;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;


@RunWith(Cucumber.class)
//@RunWith(MyReporter.class)
@CucumberOptions(
        features = {"src/test/java/com/best4cust/Runner/features/SingleCarTests.feature"},//path to the features
        
        //format ={"pretty", "html:target/cucumber-html-report"},
       	format ={"pretty", "html:target/cucumber-html-report",   "json:target/test.json",
        	    "junit:target/cucumber-html-report/cucumber_junit_report.xml"},
        tags = {"@DLGEM-639"})//what tags to include(@)/exclude(@~)

public class SingleCarTestRunner2 {

}
