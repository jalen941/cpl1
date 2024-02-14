package cplv2;

import java.util.List;

public class parser {
	scan scan;
	
	/*  public List<String> parse(String inputFileName) {
		  scan = new scan();
	     //   List<String> tokens = scan.readFile(inputFileName, "output.txt");

	        // Implement parsing logic based on grammar rules
	        // Use tokens obtained from the Scanner class

	        //return tokens;
	    }*/
	private boolean isBlockCommentStart(String token) {
	        return "/*".equals(token);
	    }
     
    
	public void getNextToken() {
		
	}
	public boolean identifierExists(String identifier) {
		return true;
	}
	public void begin() {
		
		
	}
}
