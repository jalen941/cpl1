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
	        "if", "else", "endif",
	         "FUNCTION", "not", "then",
	       "import", "variables", "begin","define",
	        "SET","is","of","type" ,"DISPLAY", "END", "MAIN", "BEGIN","int" 
	    ));

	    private ArrayList<String> specialCharacters = new ArrayList<>(Arrays.asList(
	        "=", ";",
	        "(", ")", ":", ".", ",", "\"", ">"
	    ));
	    private ArrayList<String> operators = new ArrayList<>(Arrays.asList(
		         "+", "-", "*", 
		      "\""
		    ));
	    
	    
	    
	    
	    public ArrayList<JSONObject> readFile(String inputFileName, String outputFileName) {
	        ArrayList<JSONObject> list = new ArrayList<>(); 
	        
	        try (FileWriter fileWriter = new FileWriter(outputFileName)) {
	            //System.out.println("Output file: " + outputFileName); 
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
	                        JSONObject tokenObject = createTokenObject(token); 
	                        list.add(tokenObject); 
	                    }
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	            }

	        
	            System.out.println("JSON objects created successfully.");
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	        //double check for empty data in the list
	        removeEmptyValueObjects(list);
	        //System.out.println("the list " + list);
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
	    	        	//keep empty tokens out the list
	    	            return null; 
	    	        }
	    	        
	    	        JSONObject jsonObject = new JSONObject();
	    	        
	    	        if (keywords.contains(token)) {
	    	            jsonObject.put("type", "keyword");
	    	            jsonObject.put("value", token);
	    	            
	    	        } else if (specialCharacters.contains(token)) {
	    	            jsonObject.put("type", "specialCharacter");
	    	            jsonObject.put("value", token);
	    	            
	    	        } else if (token.matches("\\d+;")) {
	    	            // separate the number and the semicolon
	    	            String number = token.substring(0, token.length() - 1); 
	    	            jsonObject.put("type", "number");
	    	            jsonObject.put("value", number);

	    	            // create a separate token for the semicolon
	    	   
	    	            JSONObject semicolonObject = new JSONObject();
	    	            semicolonObject.put("type", "specialCharacter");
	    	            semicolonObject.put("value", ";");
	    	            
	    	        }else if(operators.contains(token)) {
	    	        	jsonObject.put("type", "operator");
	    	            jsonObject.put("value", token);
	    	        }
	    	        else {
	    	            jsonObject.put("type", "identifier");
	    	            jsonObject.put("value", token);
	    	        }
	    	        System.out.println(jsonObject);
	    	        return jsonObject;
	    	    }



	
}
