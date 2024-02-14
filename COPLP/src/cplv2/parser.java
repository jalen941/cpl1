package cplv2;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

public class parser {
	scan scan;
	private ArrayList<JSONArray> tokens;
    private int currentTokenIndex;

    public parser(ArrayList<JSONArray> tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;
    }
    
	  public ArrayList<JSONArray> parse(String inputFileName) {
		  	scan = new scan();
	        ArrayList<JSONArray> tokens = scan.readFile(inputFileName, "output.txt");

	        // Implement parsing logic based on grammar rules
	        // Use tokens obtained from the Scanner class

	        return tokens;
	    }
	  
	  public JSONArray getNextToken() {
	        JSONArray nextToken = null;
	       /* while (currentTokenIndex < tokens.size()) {
	            JSONArray currentToken = tokens.get(currentTokenIndex++);
	            String type = currentToken.getString("type");
	            if (!type.equals("comment")) { // Ignore comments
	                nextToken = currentToken;
	                break;
	            }
	        }*/
	        return nextToken;
	    }

	    public boolean identifierExists(String identifier) {
	        // Iterate through the tokens to check if the identifier has already been declared
	       /* for (JSONArray token : tokens) {
	            String type = token.getString("type");
	            String value = token.getString("value");
	            if (type.equals("identifier") && value.equals(identifier)) {
	                return true;
	            }
	        }*/
	        return false;
	    }

	    private void begin() {
	        start();
	       
	    }

		private void start() {
			// TODO Auto-generated method stub
			
		}


}
