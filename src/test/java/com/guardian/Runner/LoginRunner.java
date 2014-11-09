package com.guardian.Runner;
import org.junit.runner.RunWith;

import com.guardian.Reporting.MyReporter;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;


//@RunWith(Cucumber.class)
@RunWith(MyReporter.class)
@CucumberOptions(
        features = {"src/test/resources/LoginSpecs.feature"},//path to the features
        
        //format ={"pretty", "html:target/cucumber-html-report"},
       	format ={"pretty", "html:target/cucumber-html-report",   "json:target/test.json",
        	    "junit:target/cucumber-html-report/cucumber_junit_report.xml"},
        tags = {"@runZ"})//what tags to include(@)/exclude(@~)
public class LoginRunner {

}