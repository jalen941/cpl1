package cplv2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.io.*;


public class main {
	
	  public static void main(String[] args) {
	        scan scan = new scan();
	        //String inputFileName = "C:\\Users\\Jalen\\Downloads\\welcome.scl";
	        String inputFileName = args[0];
	       
	        String outputFileName = "C:\\Users\\Jalen\\Downloads\\tokenzzz.json";

	    
	            scan.readFile(inputFileName, outputFileName);
	        
	       
	    }
    
    
}
