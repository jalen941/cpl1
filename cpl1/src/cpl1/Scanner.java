package cpl1;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scanner {
    private String sourceCode;
    private List<Token> tokens;
    private String[] keywords = {"var", "int", "float", "bool", "char"};
    private String[] specialCharacters = {"=", "+", "-", "*", "/", ";", "(", ")", ":", ".", ",", "\"", ">"};
    // Added "\"" to specialCharacters array

    public Scanner(String sourceCode) {
        this.sourceCode = sourceCode;
        this.tokens = new ArrayList<>();
    }

    public List<Token> scan() throws SyntaxError {
        sourceCode = sourceCode.trim();
        while (!sourceCode.isEmpty()) {
            if (startsWithAny(specialCharacters)) {
                tokens.add(new Token(String.valueOf(sourceCode.charAt(0)), String.valueOf(sourceCode.charAt(0))));
                sourceCode = sourceCode.substring(1);
            } else if (Character.isLetter(sourceCode.charAt(0))) {
                Pattern pattern = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*");
                Matcher matcher = pattern.matcher(sourceCode);
                if (matcher.lookingAt()) {
                    String identifier = matcher.group();
                    if (isKeyword(identifier)) {
                        tokens.add(new Token("KEYWORD", identifier));
                    } else {
                        tokens.add(new Token("IDENTIFIER", identifier));
                    }
                    sourceCode = sourceCode.substring(identifier.length());
                } else {
                    throw new SyntaxError("Invalid identifier");
                }
            } else if (Character.isDigit(sourceCode.charAt(0))) {
                Pattern pattern = Pattern.compile("\\d+");
                Matcher matcher = pattern.matcher(sourceCode);
                if (matcher.lookingAt()) {
                    String integerLiteral = matcher.group();
                    tokens.add(new Token("INTEGER", integerLiteral));
                    sourceCode = sourceCode.substring(integerLiteral.length());
                } else {
                    throw new SyntaxError("Invalid integer literal");
                }
            } else if (sourceCode.startsWith("\"")) {
                // Handle string literals
                int endIndex = sourceCode.indexOf("\"", 1);
                if (endIndex == -1) {
                    throw new SyntaxError("Unterminated string literal");
                }
                String stringLiteral = sourceCode.substring(0, endIndex + 1);
                tokens.add(new Token("STRING_LITERAL", stringLiteral));
                sourceCode = sourceCode.substring(endIndex + 1);
            } else if (Character.isWhitespace(sourceCode.charAt(0))) {
                sourceCode = sourceCode.trim();
            } else {
                throw new SyntaxError("Unexpected character: " + sourceCode.charAt(0));
            }
        }
        return tokens;
    }

    private boolean startsWithAny(String[] strings) {
        for (String s : strings) {
            if (sourceCode.startsWith(s)) {
                return true;
            }
        }
        return false;
    }

    private boolean isKeyword(String identifier) {
        for (String keyword : keywords) {
            if (keyword.equals(identifier)) {
                return true;
            }
        }
        return false;
    }
}
