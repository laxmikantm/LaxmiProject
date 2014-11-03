package com.guardian.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;



import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellUtil;



public class TestDataReader {
	
		
public String[][] readExcel(String filePath,String fileName,String sheetName) throws IOException{
	    	
			 String[][] tabArray = null;
			 
	    	File file = new File((System.getProperty("user.dir")+"/TestData.xls"));
	    	FileInputStream inputStream = new FileInputStream(file);
	    	Workbook theWorkbook = null;

		 
		        theWorkbook = new HSSFWorkbook(inputStream);
		 
		    //Read sheet inside the workbook by its name
		 
		    //Sheet theSheet = theWorkbook.getSheet(sheetName);
		      Sheet theSheet= theWorkbook.getSheet("TestData");
		 
		    //Find number of rows in excel file
		 
		    int rowCount = theSheet.getLastRowNum()-theSheet.getFirstRowNum();
		    
		    
			tabArray = new String[14][2];
		    
		    
		    
		    
		 
		    //Create a loop over all the rows of excel file to read it
		 
		    for (int i = 0; i < rowCount+1; i++) {
		 
		        Row row = theSheet.getRow(i);
		 
		        //Create a loop to print cell values in a row
		 
		        for (int j = 0; j < row.getLastCellNum(); j++) {
		 
		        	tabArray[i][j]=row.getCell(j).getStringCellValue();
		 
		            System.out.print(row.getCell(j).getStringCellValue()+"|| ");
		 
		        }
		 
		        System.out.println();
		 
		    }
		 
		    System.out.println("\n Title Field Value ="+tabArray[0][0]+tabArray[0][1]);
		    System.out.println("\n First Row values ="+tabArray[1][0]+tabArray[1][1]);
		    System.out.println("\n second Row values ="+tabArray[2][0]+tabArray[2][1]);
		    
		  return tabArray;   
		 
		    }
}
	    
