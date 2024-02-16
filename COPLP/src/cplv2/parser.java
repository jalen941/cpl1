package cplv2;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import org.json.JSONObject;

public class parser {
    private ArrayList<JSONObject> tokens;
    private int currentTokenIndex;

    // Constants for token types
    private static final int IDENTIFIER_TYPE = 0;
    private static final int NUMBER_TYPE = 1;
    private static final int STRING_LITERAL_TYPE = 2;

    public parser(ArrayList<JSONObject> tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;
    }

    public void parse() {
        parseProgram();
        if (currentTokenIndex < tokens.size()) {
            System.out.println("Error: Unexpected tokens remaining.");
        }
    }

    private void parseProgram() {
        parseImportSection();
      //  parseImplementationSection();
       // parseFunctionDeclaration();
    }

    private void parseImportSection() {
        getNextToken("import");
        getNextToken(STRING_LITERAL_TYPE);
    }

    private void parseImplementationSection() {
        getNextToken("implementations");
    }

    private void parseFunctionDeclaration() {
        getNextToken("function");
        getNextToken(IDENTIFIER_TYPE);
        getNextToken("is");
        parseVariablesSection();
        getNextToken("begin");
        parseStatementList();
        getNextToken("endfun");
        getNextToken(IDENTIFIER_TYPE);
    }

    private void parseVariablesSection() {
        getNextToken("variables");
        while (isToken("define")) {
            parseVariableDeclaration();
        }
    }

    private void parseVariableDeclaration() {
        getNextToken("define");
        getNextToken(IDENTIFIER_TYPE);
        getNextToken("of");
        getNextToken("type");
        parseDataType();
    }

    private void parseDataType() {
        if (isToken("double") || isToken("pointer")) {
            getNextToken();
        } else {
            System.out.println("Error: Expected data type");
        }
    }

    private void parseStatementList() {
        while (!isToken("endfun") && !isToken("endif")) {
            parseStatement();
        }
    }

    private void parseStatement() {
        if (isToken("display")) {
            getNextToken("display");
            getNextToken(STRING_LITERAL_TYPE);
        } else if (isToken("set")) {
            getNextToken("set");
            getNextToken(IDENTIFIER_TYPE);
            getNextToken("=");
            parseExpression();
        } else if (isToken("input")) {
            getNextToken("input");
            getNextToken(STRING_LITERAL_TYPE);
            getNextToken(",");
            getNextToken(IDENTIFIER_TYPE);
        } else if (isToken("if")) {
            getNextToken("if");
            parseExpression();
            getNextToken("then");
            parseStatementList();
            getNextToken("endif");
        } else if (isToken("return")) {
            getNextToken("return");
            getNextToken(NUMBER_TYPE);
        }
    }

    private void parseExpression() {
        if (isToken(IDENTIFIER_TYPE) || isToken(NUMBER_TYPE) || isToken(STRING_LITERAL_TYPE)) {
            getNextToken();
        } else {
            parseExpression();
            parseComparisonOperator();
            parseExpression();
        }
    }

    private void parseComparisonOperator() {
        if (isToken(">=") || isToken(">") || isToken("<=") || isToken("<") || isToken("==") || isToken("!=")) {
            getNextToken();
        } else {
            System.out.println("Error: Expected comparison operator");
        }
    }

    private void getNextToken(String expectedToken) {
        JSONObject token = getNextToken();
        if (token == null || !token.optString("value").equals(expectedToken)) {
            System.out.println("Error: Unexpected token: " + token.optString("value") + ". Expected: " + expectedToken);
        }
    }

    private void getNextToken(int expectedType) {
        JSONObject token = getNextToken();
        if (token == null || token.optInt("type") != expectedType) {
            System.out.println("Error: Unexpected token type: " + token.optInt("type") + " " + token.optString("value")+ ". Expected: " + expectedType);
        }
    }

    private JSONObject getNextToken() {
        if (currentTokenIndex < tokens.size()) {
        	System.out.println("next token :"+ tokens.get(0));
        	
        
            return tokens.get(currentTokenIndex++);
        } else {
            return null;
        }
    }

    private boolean isToken(String expectedToken) {
        JSONObject token = getNextToken();
        return token != null && token.optString("value").equals(expectedToken);
    }

    private boolean isToken(int expectedType) {
        JSONObject token = getNextToken();
        return token != null && token.optInt("type") == expectedType;
    }
}