package cplv2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class scan {
	private ArrayList<String> keywords = new ArrayList<>(Arrays.asList(
	        "if", "else", "var", "input", "endif",
	        "return", "function", "endfun", "not", "then",
	        "display", "define", "import", "variables", "begin",
	        "set"
	    ));

	    private ArrayList<String> specialCharacters = new ArrayList<>(Arrays.asList(
	        "=", "+", "-", "*", "/", ";",
	        "(", ")", ":", ".", ",", "\"", ">"
	    ));
	    
	    
	    
	    
	    
	    public ArrayList<JSONObject> readFile(String inputFileName, String outputFileName) {
	        ArrayList<JSONObject> list = new ArrayList<>(); // List to hold JSONObjects
	        try (FileWriter fileWriter = new FileWriter(outputFileName)) {
	            System.out.println("Output file: " + outputFileName); // Debugging output
	            try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
	                String line;
	                boolean insideBlockComment = false;

	                while ((line = reader.readLine()) != null) {
	                    line = removeLineComments(line);

	                    if (insideBlockComment) {
	                        if (line.contains("*/")) {
	                            insideBlockComment = false;
	                            line = line.substring(line.indexOf("*/") + 2);
	                        } else {
	                            continue;
	                        }
	                    }

	                    String[] tokens = line.split("\\s+");
	                    for (String token : tokens) {
	                        if (isBlockCommentStart(token)) {
	                            insideBlockComment = true;
	                            break;
	                        }
	                        JSONObject tokenObject = createTokenObject(token); // Create a JSONObject for the token
	                        list.add(tokenObject); // Add the JSONObject to the list
	                    }
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	            }

	            // Write the list of JSON objects to file (if needed)
	            // This part depends on your requirement
	            // You can write the JSON objects to file in a format you desire

	            System.out.println("JSON objects created successfully.");
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	      
	        removeEmptyValueObjects(list);
	        System.out.println("the list " + list);
	        return list;
	    }
	    public static void removeEmptyValueObjects(ArrayList<JSONObject> list) {
	        ArrayList<JSONObject> toRemove = new ArrayList<>();
	        for (JSONObject obj : list) {
	            if (obj.optString("value").isEmpty()) {
	                toRemove.add(obj);
	            }
	        }
	        list.removeAll(toRemove);
	    }
	    	    private String removeLineComments(String line) {
	    	        return line.replaceAll("//.*", "");
	    	    }

	    	    private boolean isBlockCommentStart(String token) {
	    	        return "/*".equals(token);
	    	    }
	
	
	 private JSONObject createTokenObject(String token) {
	        JSONObject jsonObject = new JSONObject();
	        if (keywords.contains(token)) {
	            jsonObject.put("type", "keyword");
	            jsonObject.put("value", token);
	            System.out.println(jsonObject);
	        } else if (specialCharacters.contains(token)) {
	            jsonObject.put("type", "specialCharacter");
	            jsonObject.put("value", token);
	            System.out.println(jsonObject);
	        } else if (token.startsWith("\"") && token.endsWith("\"")) { // Check if token is a string literal
	            jsonObject.put("type", "stringLiteral");
	            jsonObject.put("value", token.substring(1, token.length() - 1)); // Remove quotes from the string literal
	        
	        }
	        else {
	            jsonObject.put("type", "identifier");
	            jsonObject.put("value", token);
	            System.out.println(jsonObject);
	        }
	        return jsonObject;
	    }
	
}
