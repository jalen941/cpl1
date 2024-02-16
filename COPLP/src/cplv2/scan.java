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
	        "set","is","of","type"
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
	            if (obj == null || obj.optString("value").isEmpty()) {
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
	    	        if (token.isEmpty()) {
	    	            return null; // Skip empty tokens
	    	        }
	    	        
	    	        JSONObject jsonObject = new JSONObject();
	    	        
	    	        if (keywords.contains(token)) {
	    	            jsonObject.put("type", "keyword");
	    	            jsonObject.put("value", token);
	    	        } else if (specialCharacters.contains(token)) {
	    	            jsonObject.put("type", "specialCharacter");
	    	            jsonObject.put("value", token);
	    	        } else if (token.matches("\\d+;")) { // Check if token is a number followed by a semicolon
	    	            // Separate the number and the semicolon
	    	            String number = token.substring(0, token.length() - 1); // Extract the number part
	    	            jsonObject.put("type", "number");
	    	            jsonObject.put("value", number);

	    	            // Create a separate token for the semicolon
	    	            JSONObject semicolonObject = new JSONObject();
	    	            semicolonObject.put("type", "specialCharacter");
	    	            semicolonObject.put("value", ";");
	    	            // You might want to add this semicolonObject to the list of tokens too.
	    	        } else if (token.startsWith("\"") && token.endsWith("\"")) {
	    	            jsonObject.put("type", "stringLiteral");
	    	            jsonObject.put("value", token.substring(1, token.length() - 1));
	    	        } else {
	    	            jsonObject.put("type", "identifier");
	    	            jsonObject.put("value", token);
	    	        }
	    	        
	    	        return jsonObject;
	    	    }



	
}
