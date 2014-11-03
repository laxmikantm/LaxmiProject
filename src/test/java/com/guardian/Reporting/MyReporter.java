package com.guardian.Reporting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.json.simple.parser.JSONParser;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import cucumber.runtime.ClassFinder;
import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.RuntimeOptionsFactory;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;
import cucumber.runtime.junit.Assertions;
import cucumber.runtime.junit.FeatureRunner;
import cucumber.runtime.junit.JUnitReporter;
import cucumber.runtime.model.CucumberFeature;
import cucumber.runtime.snippets.SummaryPrinter;

public class MyReporter extends Cucumber {
	private final JUnitReporter jUnitReporter;

	private final List<FeatureRunner> children = new ArrayList<FeatureRunner>();

	private final Runtime runtime;

	public MyReporter(Class clazz) throws InitializationError, IOException {

		super(clazz);
		ClassLoader classLoader = clazz.getClassLoader();
		Assertions.assertNoCucumberAnnotatedMethods(clazz);

		RuntimeOptionsFactory runtimeOptionsFactory = new RuntimeOptionsFactory(clazz, new Class[] { CucumberOptions.class,Options.class });
		RuntimeOptions runtimeOptions = runtimeOptionsFactory.create();

		ResourceLoader resourceLoader = new MultiLoader(classLoader);
		ClassFinder classFinder = new ResourceLoaderClassFinder(resourceLoader, classLoader);
		runtime = new Runtime(resourceLoader, classFinder, classLoader, runtimeOptions);

		jUnitReporter = new JUnitReporter(runtimeOptions.reporter(classLoader), runtimeOptions.formatter(classLoader),
				runtimeOptions.isStrict());
		addChildren(runtimeOptions.cucumberFeatures(resourceLoader));

	}

	@Override
	public void run(RunNotifier notifier) {

		super.run(notifier);
		jUnitReporter.done();
		jUnitReporter.close();
		new SummaryPrinter(System.out).print(runtime);

		processReport();
	}

	private void processReport() {

		JSONParser parser = new JSONParser();

		try {

			FileReader fileReader = new FileReader("target/test.json");
			BufferedReader br = new BufferedReader(fileReader);
			StringBuilder builder = new StringBuilder();
			String text;
			while ((text = br.readLine()) != null) {
				builder.append(text);
			}
			int index = builder.indexOf("{");
			builder = builder.replace(0, index, "{\"results\": [");
			builder.append("}");
			int lastIndex = builder.lastIndexOf("]");

			// builder = builder.replace(0, index, "");
			// int lastIndex = builder.lastIndexOf("]");
			// String content = builder.substring(0, lastIndex);

			String content = builder.toString();

			System.out.println("### content: ###" + content);
			reportGenerator(content);
			// generateReport(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	private void reportGenerator(String content) throws IOException {

		File file = new File("target/index.html");
		file.createNewFile();

		FileWriter writer = new FileWriter(file);
		writer.write("<!doctype html> <html> <head> <meta charset='UTF-8'> <style type='text/css'> \n");
		writer.write("table{ width:80%; border-collapse: collapse; background-color: #fff; border:1px solid #555; } \n");
		writer.write("th{ color:#fff; background-color: #555; } tr:nth-child(odd){ background-color: #f5f5f5; } td{ border-left:1px solid #ccc; } \n");
		writer.write("td.failed{background-color:#FF6347;} td.skipped{background-color:#EE82EE;} td.passed{background-color:#8FBC8F;} \n");
		writer.write("tr.failed{background-color:#FF6347;} tr.passed{background-color:#8FBC8F;} \n");
		writer.write("a:link { color: #000000; } a:visited { color: #000000; } a:hover { color: #000000; } a:active { color: #000000; }");
		writer.write("</style> </head> <body>\n");

		JsonFactory f = new MappingJsonFactory();
		JsonParser jp = f.createJsonParser(content);

		JsonToken current;

		current = jp.nextToken();
		if (current != JsonToken.START_OBJECT) {
			System.out.println("Error: root should be object: quiting.");
			return;
		}

		JsonNode node = jp.readValueAsTree();
		JsonNode nodeArray = node.get("results");
		Iterator<JsonNode> itr = nodeArray.iterator();
//		StringBuilder summaryReportBuilder = new StringBuilder();
		StringBuilder detailedReportBuilder = new StringBuilder();
		detailedReportBuilder.append("<u><h2>Detailed Report</h2></u>\n");
		while (itr.hasNext()) {
			JsonNode arrayNode = itr.next();
			String featureReport = processFeatureReport(arrayNode.toString());
			detailedReportBuilder.append(featureReport);
		}
		
		writer.append(buildSummary(featureResultMap));
//		summaryReportBuilder = processFeatureForSummary(featureResultMap);
//		writer.write(summaryReportBuilder.toString());
		writer.write(detailedReportBuilder.toString());
		
		System.out.println("### Size: " + featureResultMap);
		System.out.println("### Size: " + featureResultMap.size());
		
		writer.write("</body></html>");
		writer.flush();
		writer.close();
	}
	private String processFeatureReport(String content) throws IOException {

		StringBuilder builder = new StringBuilder();
		
		JsonFactory f = new MappingJsonFactory();
		JsonParser jp = f.createJsonParser(content);

		JsonToken current;

		current = jp.nextToken();
		if (current != JsonToken.START_OBJECT) {
			System.out.println("Error: root should be object: quiting.");
			return "";
		}

		List<ScenarioResultSummary> scenarioResultsList = null;

		String featureName = null;
		
		while (jp.nextToken() != JsonToken.END_OBJECT) {
			String fieldName = jp.getCurrentName();
			current = jp.nextToken();

			if(fieldName.equals("name")) {
				featureName = jp.getText();
			}
			
			if (fieldName.equals("keyword") && jp.getText().equals("Feature")) {
				scenarioResultsList = new ArrayList<ScenarioResultSummary>();
				featureResultMap.put(featureName, scenarioResultsList);
				featureName = null;
			}

			if (fieldName.equals("elements")) {
				if (current == JsonToken.START_ARRAY) {

					while (jp.nextToken() != JsonToken.END_ARRAY) {
						ScenarioResultSummary scenarioResultSummary = null;
						JsonNode node = jp.readValueAsTree();
						try {
							StringBuilder scenarioResult = new StringBuilder("passed");

							String annotationName = node.get("tags").get(0).get("name").getTextValue();
							builder.append("<br><br><h4><u> <a id="+annotationName+">" + annotationName + "</a>  :  " + node.get("name").getTextValue() + "</u></h4>");
							builder.append(processStepsArrayNode(node.get("steps"), scenarioResult));

							scenarioResultSummary = new ScenarioResultSummary();
							scenarioResultSummary.setAnnotation(annotationName);
							scenarioResultSummary.setResult(scenarioResult.toString());
						} catch (NullPointerException npe) {
							npe.printStackTrace();
							jp.skipChildren();
						}
						if (scenarioResultsList != null && scenarioResultSummary != null)
							scenarioResultsList.add(scenarioResultSummary);
					}
				} else {
					System.out.println("Error: records should be an array: skipping.");
					jp.skipChildren();
				}
			} else {
				System.out.println("Unprocessed property: " + fieldName);
				jp.skipChildren();
			}
		}

		return builder.toString();
	}

	private String processStepsArrayNode(JsonNode nodeArray, StringBuilder scenarioResult) {

		StringBuilder builder = new StringBuilder();

		builder.append("<table>"
				+ " <tr> <th width='100'>Keyword</th> <th width='400'>Name</th> <th width='100'>Status</th> <th width='100'>Exception</th></tr> \n");
		Iterator<JsonNode> itr = nodeArray.iterator();
		while (itr.hasNext()) {
			JsonNode node = itr.next();
			String result = node.get("result").get("status").getTextValue();
			builder.append("<tr class="+result+">");
			builder.append("<td>" + node.get("keyword").toString().replace("\"", "") + "</td>");
			builder.append("<td>" + node.get("name").toString().replace("\"", "") + "</td>");
			builder.append("<td>" + result + "</td>");
			StringBuilder exception = new StringBuilder();
			if (result.equalsIgnoreCase("failed")) {
				exception.append(node.get("result").get("error_message").getTextValue());
				appendNewLine(exception);
//				replaceAll(exception, "at", "\n at");
				System.out.println("###: "+exception);
				int length = scenarioResult.length();
				scenarioResult = scenarioResult.replace(0, length, result);
			}
			builder.append("<td>" + exception.toString() + "</td>");
			builder.append("</tr> \n");
		}
		builder.append("</table>");
		return builder.toString();
	}
	
	private String buildSummary(HashMap featureResultMap) {
		StringBuilder builder = new StringBuilder();
		builder.append("<h2><u>Summary</u></h2>");
		builder.append("<table border='1'> <tr> <th>Feature</th> <th>Scenario</th> <th>Pass</th> <th>Fail</th> <th>Skip</th> <th>Total</th>  <th>Percentage</th></tr> \n");
		builder.append(processFeatureForSummary(featureResultMap));
		builder.append("</table>");
		return builder.toString();
	}

	private HashMap<String, List<ScenarioResultSummary>> featureResultMap = new HashMap<String, List<ScenarioResultSummary>>();

	private StringBuilder processFeatureForSummary(HashMap featureResultMap) {
		StringBuilder builder = new StringBuilder();
		Iterator<Map.Entry> itr = featureResultMap.entrySet().iterator();
		while (itr.hasNext()) {
			StringBuilder isFeaturePass = new StringBuilder();;
			Map.Entry pairs = (Map.Entry)itr.next();
			String str = pairs.getKey().toString();
			List<ScenarioResultSummary> value = (List<ScenarioResultSummary>)pairs.getValue();
			builder.append("<tr><td rowspan="+value.size()+">" + str + "</td>");
			String scenarioResultForSummary = processScenarioListForSummary(value, isFeaturePass);
//			builder.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			builder.append(scenarioResultForSummary);
//			builder.append("<td>"+isFeaturePass+"</td>");
//			builder.append("</tr>");
//			builder.append(scenarioResultForSummary);
		}
		return builder;
	}
	
	private String processScenarioListForSummary(List<ScenarioResultSummary> listOfScenariosResults, StringBuilder isFeaturePass) {
		StringBuilder builder = new StringBuilder();
		
		int scenariosPassCount = 0;
		int scenariosFailCount = 0;
		int scenariosSkipCount = 0;
		
		int i = 0;
		int indexValue = 0;
		
		for(ScenarioResultSummary scenarioResult: listOfScenariosResults) {
			String result = scenarioResult.getResult();
			isFeaturePass.append(result);
//			isFeaturePass = result.equalsIgnoreCase("failed") ? "failed" : "passed";
//			builder.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			String annotation = scenarioResult.getAnnotation();
			builder.append("<td class="+result+"> <a href='#"+annotation+"'>" + annotation + "</td>");
			if(i == 0) {
				indexValue = builder.length();
				i++;
			}
//			builder.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			builder.append("</tr>\n");
			if(result.equalsIgnoreCase("passed")) { 
				scenariosPassCount ++;
			} else if (result.equalsIgnoreCase("failed")) {
				scenariosFailCount ++;
			} else {
				scenariosSkipCount ++;
			}
		}
		int totalScenarios = listOfScenariosResults.size();
		builder.insert(indexValue, resultForSummary(scenariosPassCount, scenariosFailCount, scenariosSkipCount, totalScenarios));
		return builder.toString();
	}
	
	
	private String resultForSummary(int passCount, int failCount, int skipCount, int rowCount) {
		int totalCount = passCount + failCount + skipCount;
		float passPercentage = 0.0f;
		if (totalCount != 0)
			passPercentage = (passCount / totalCount ) * 100;
		
		StringBuilder updatedResult = new StringBuilder();
			updatedResult.append("<td align = 'center' rowspan = "+rowCount+">" + passCount + "</td>");
			updatedResult.append("<td align = 'center' rowspan = "+rowCount+">" + failCount + "</td>");
			updatedResult.append("<td align = 'center' rowspan = "+rowCount+">" + skipCount + "</td>");
			updatedResult.append("<td align = 'center' rowspan = "+rowCount+">" + totalCount + "</td>");
			updatedResult.append("<td align = 'center' rowspan = "+rowCount+">" + passPercentage + "</td>");
		return updatedResult.toString();
	}
	
	private void addChildren(List<CucumberFeature> cucumberFeatures) throws InitializationError {

		for (CucumberFeature cucumberFeature : cucumberFeatures) {
			children.add(new FeatureRunner(cucumberFeature, runtime, jUnitReporter));
		}
	}

	private void appendNewLine(StringBuilder builder) {
		int length = builder.length();
		int itr = 75;
		
		while (itr < length) {
			builder.insert(itr, "\n");
			itr = itr + 75;
		}
	}
	
	private void replaceAll(StringBuilder builder, String from, String to)
	{
	    int index = builder.indexOf(from);
	    while (index != -1)
	    {
	        builder.replace(index, index + from.length(), to);
	        index += to.length(); // Move to the end of the replacement
	        index = builder.indexOf(from, index);
	    }
	}	
	
	public class ScenarioResultSummary {

		public String getResult() {

			return result;
		}

		public void setResult(String result) {

			this.result = result;
		}

		public String getAnnotation() {

			return annotation;
		}

		public void setAnnotation(String annotation) {

			this.annotation = annotation;
		}

		public String getRemarks() {

			return remarks;
		}

		public void setRemarks(String remarks) {

			this.remarks = remarks;
		}

		private String result;

		private String annotation;

		private String remarks;

	}

}
