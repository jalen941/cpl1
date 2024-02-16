package cplv2;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import java.util.ArrayList;

public class parser {
    private ArrayList<JSONObject> tokens;
    private int currentTokenIndex;

    public parser(ArrayList<JSONObject> tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;
    }

    public void begin() {
        start();
    }

    private void start() {
        parseProgram();
    }

    private void parseProgram() {
        parseFunctionDefinition();
    }

    private void parseFunctionDefinition() {
        if (!match("keyword", "function")) {
            reportError("Expected 'function' keyword.");
            return;
        }
        if (!match("identifier")) {
            reportError("Expected function identifier after 'function' keyword.");
            return;
        }
        if (!match("keyword", "is")) {
            reportError("Expected 'is' keyword after function identifier.");
            return;
        }
        parseVariablesBlock();
       parseBeginStatement();
    }
    private void parseVariablesBlock() {
        if (!match("keyword", "variables")) {
            reportError("Expected 'variables' keyword.");
            return;
        }
        while (true) {
            if (currentTokenIndex >= tokens.size()) {
                reportError("Expected 'begin' keyword before reaching the end of the token list.");
                return;
            }
            
            if (match("keyword", "begin")) {
                return; // 'begin' keyword found, exit the loop
            }

            if (!match("keyword", "define")) {
                reportError("Expected 'define' keyword.");
                return;
            }
            if (!match("identifier")) {
                reportError("Expected identifier after 'define' keyword.");
                return;
            }
            if (!match("keyword", "of")) {
                reportError("Expected 'of' keyword after variable identifier.");
                return;
            }
            if (!match("keyword", "type")) {
                reportError("Expected 'type' keyword after 'of'.");
                return;
            }
            if (!match("identifier")) {
                reportError("Expected type identifier after 'type' keyword.");
                return;
            }

            // Print debug information
            System.out.println("Parsed variable definition successfully.");
        }
    }



    private void parseBeginStatement() {
        if (!match("keyword", "begin")) {
            //reportError("Expected 'begin' keyword.");
           // System.out.println("at " + currentTokenIndex);
            //System.out.println("at " + tokens.get(currentTokenIndex));

            if (currentTokenIndex < tokens.size()) {
               // System.out.println(tokens.get(currentTokenIndex));
            } else {
                System.out.println("Reached end of token list.");
            }
            return;
        }
        parseStatement();
    }


    private void parseStatement() {
        if (!match("keyword", "display")) {
            reportError("Expected 'display' keyword.");
            return;
        }
        if (!match("stringLiteral")) {
            reportError("Expected string literal after 'display' keyword.");
            return;
        }
        if (!match("specialCharacter", ";")) {
            reportError("Expected ';' after string literal.");
            return;
        }
    }

    private boolean match(String expectedType, String expectedValue) {
        if (currentTokenIndex < tokens.size()) {
            JSONObject token = tokens.get(currentTokenIndex);
            String type = token.optString("type");
            String value = token.optString("value");
       
            if (type.equals(expectedType) && value.equals(expectedValue)) {
            //System.out.println("returned true");
            	System.out.println("type: "+ type + " value: "+ value+ " expected type " + expectedType+ " expected value " + expectedValue);
                currentTokenIndex++;
                return true;
            }
            
           // System.out.println("returned false");
           // System.out.println("type: "+ type + " value: "+ value+ " expected type " + expectedType+ " expected value " + expectedValue);
        }
        return false;
    }

    private boolean match(String expectedType) {
        if (currentTokenIndex < tokens.size()) {
            JSONObject token = tokens.get(currentTokenIndex);
            String type = token.optString("type");
            
            if (type.equals(expectedType)) {
                currentTokenIndex++;
                return true;
            }
        }
        return false;
    }

    private void reportError(String message) {
        System.out.println("Syntax Error: " + message);
    }
}