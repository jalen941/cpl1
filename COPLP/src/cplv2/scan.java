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
	    
	    
	    
	    
	    
	    public ArrayList<JSONArray> readFile(String inputFileName, String outputFileName) {
	    	ArrayList list = new ArrayList<>();
	    	int index=0;
	        try (FileWriter fileWriter = new FileWriter(outputFileName)) {
	            System.out.println("Output file: " + outputFileName); // Debugging output
	            JSONArray jsonArray = new JSONArray();
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
	                        jsonArray.put(createTokenObject(token));
	                        list.add(jsonArray.get(index));
	                        index++;
	                    }
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	            }

	            fileWriter.write(jsonArray.toString(4)); 
	            System.out.println("JSON file created successfully.");
	            return list;
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			return list;
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
	        } else {
	            jsonObject.put("type", "identifier");
	            jsonObject.put("value", token);
	            System.out.println(jsonObject);
	        }
	        return jsonObject;
	    }
	
}
