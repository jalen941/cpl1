package cpl1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;


public class SCLScanner {
    public static void main(String[] args) {
        if (args.length != 1) {
        	//"C:\Users\Jalen\Downloads\welcome.scl"
            //System.out.println("Usage: java SCLScanner <input_file>");
            //System.exit(1);
        }

       // String inputFileName = args[0];
        String inputFileName = "C:\\Users\\Jalen\\Downloads\\welcome.scl";

        StringBuilder sourceCodeBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
            String line;

            while ((line = reader.readLine()) != null) {
                sourceCodeBuilder.append(line).append("\n");
                //System.out.println("Usage: java SCLScanner <input_file>");

            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            System.exit(1);
        }

        String sourceCode = sourceCodeBuilder.toString();
        Scanner scanner = new Scanner(sourceCode);
        List<Token> tokens;
        try {
            tokens = scanner.scan();
      
        } catch (SyntaxError e) {
            System.out.println("Syntax error: " + e.getMessage());
            return;
        }

        JSONArray jsonArray = new JSONArray();
        for (Token token : tokens) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", token.type);
            jsonObject.put("value", token.value);
            jsonArray.put(jsonObject);
            System.out.println(token);
        }

        String outputFileName = inputFileName.split("\\.")[0] + "_tokens.json";
        try (FileWriter fileWriter = new FileWriter(outputFileName)) {
            fileWriter.write(jsonArray.toString());
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}