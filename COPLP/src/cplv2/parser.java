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

    public void parse() {
        parseProgram();
    }

    private void parseProgram() {
        parseFunctionDefinition();
      // parseEndfunStatement();
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
        parseBeginStatement(); // Moved the parsing of the "begin" statement here
    }
 

    private void parseVariablesBlock() {
        if (!match("keyword", "variables")) {
            reportError("Expected 'variables' keyword.");
            return;
        }
        while (match("keyword", "define")) {
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
        }
    }

    private void parseBeginStatement() {
        if (!match("keyword", "begin")) {
           // reportError("Expected 'begin' keyword.");
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
        // Add more parsing logic for other statement types if needed
    }

    private boolean match(String expectedType, String expectedValue) {
        if (currentTokenIndex < tokens.size()) {
            JSONObject token = tokens.get(currentTokenIndex);
            String type = token.optString("type");
            String value = token.optString("value");
            if (type.equals(expectedType) && value.equals(expectedValue)) {
                currentTokenIndex++;
                return true;
            }
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