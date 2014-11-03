package com.guardian.utilities;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;




public class XMLDataReader  {

	@SuppressWarnings("finally")
	public String searchElement(String ParentNode, String searchTerm)
	{
		String result = null;

	 
		try {

			File fXmlFile = new File((System.getProperty("user.dir")+"/src/test/java/com/guardian/testData/testData.xml"));
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			
			NodeList nlist= doc.getElementsByTagName(ParentNode);

			for (int temp = 0; temp < nlist.getLength(); temp++) {
				Node nNode = nlist.item(temp);
		 
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		 
					Element eElement = (Element) nNode;
					
					result=eElement.getElementsByTagName(searchTerm).item(0).getTextContent();
		 
				}
			}
		    } catch (Exception e) {
			e.printStackTrace();
		    } finally
            {
		    	return result;
		    }
	}
}
