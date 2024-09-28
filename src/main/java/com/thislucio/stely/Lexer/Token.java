package com.thislucio.stely.Lexer;

public class Token {
    public String text;
    public TokenType kind;

    public Token(String tokenText, TokenType tokenKind) {
        this.text = tokenText;
        this.kind = tokenKind;
    }

    public static TokenType checkIfKeyword(String tokenText) {
        for (TokenType kind : TokenType.values()) {
            if (kind.name().equals(tokenText) && kind.getValue() >= 100 && kind.getValue() < 200) {
                return kind;
            }
        }
        return null;
    }
}