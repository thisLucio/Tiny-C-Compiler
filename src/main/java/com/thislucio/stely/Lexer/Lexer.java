package com.thislucio.stely.Lexer;

public class Lexer {
    private String source;
    private char curChar;
    private int curPos;

    public Lexer(String source) {
        this.source = source + "\n"; // Adiciona nova linha ao final do código fonte
        this.curChar = '\0';         // Inicializa com o caractere vazio
        this.curPos = -1;            // Posição inicial antes do primeiro caractere
        nextChar();                  // Chama o método nextChar
    }

    // Processa o próximo caractere
    public void nextChar() {
        curPos++;
        if (curPos >= source.length()) {
            curChar = '\0';  // EOF
        } else {
            curChar = source.charAt(curPos);  // Pega o próximo caractere
        }
    }

    // Retorna o caractere seguinte sem avançar o cursor
    public char peek() {
        if (curPos + 1 >= source.length()) {
            return '\0';
        }
        return source.charAt(curPos + 1);
    }

    // Aborta com uma mensagem de erro
    public void abort(String message) {
        throw new RuntimeException("Lexing error. " + message);
    }

    // Ignora espaços em branco, exceto novas linhas
    public void skipWhitespace() {
        while (curChar == ' ' || curChar == '\t' || curChar == '\r') {
            nextChar();
        }
    }

    // Ignora comentários
    public void skipComment() {
        if (curChar == '#') {
            while (curChar != '\n') {
                nextChar();
            }
        }
    }

    // Retorna o próximo token
    public Token getToken() {
        skipWhitespace();
        skipComment();
        Token token = null;

        switch (curChar) {
            case '+':
                token = new Token(Character.toString(curChar), TokenType.PLUS);
                break;
            case '-':
                token = new Token(Character.toString(curChar), TokenType.MINUS);
                break;
            case '*':
                token = new Token(Character.toString(curChar), TokenType.ASTERISK);
                break;
            case '/':
                token = new Token(Character.toString(curChar), TokenType.SLASH);
                break;
            case '=':
                if (peek() == '=') {
                    char lastChar = curChar;
                    nextChar();
                    token = new Token(lastChar + Character.toString(curChar), TokenType.EQEQ);
                } else {
                    token = new Token(Character.toString(curChar), TokenType.EQ);
                }
                break;
            case '>':
                if (peek() == '=') {
                    char lastChar = curChar;
                    nextChar();
                    token = new Token(lastChar + Character.toString(curChar), TokenType.GTEQ);
                } else {
                    token = new Token(Character.toString(curChar), TokenType.GT);
                }
                break;
            case '<':
                if (peek() == '=') {
                    char lastChar = curChar;
                    nextChar();
                    token = new Token(lastChar + Character.toString(curChar), TokenType.LTEQ);
                } else {
                    token = new Token(Character.toString(curChar), TokenType.LT);
                }
                break;
            case '!':
                if (peek() == '=') {
                    char lastChar = curChar;
                    nextChar();
                    token = new Token(lastChar + Character.toString(curChar), TokenType.NOTEQ);
                } else {
                    abort("Expected !=, got !" + peek());
                }
                break;
            case '"':
                nextChar();
                int startPos = curPos;
                while (curChar != '"') {
                    if (curChar == '\r' || curChar == '\n' || curChar == '\t' || curChar == '\\' || curChar == '%') {
                        abort("Illegal character in string.");
                    }
                    nextChar();
                }
                String tokText = source.substring(startPos, curPos);
                token = new Token(tokText, TokenType.STRING);
                break;
            case '\n':
                token = new Token(Character.toString(curChar), TokenType.NEWLINE);
                break;
            case '\0':
                token = new Token("", TokenType.EOF);
                break;
            default:
                if (Character.isDigit(curChar)) {
                    startPos = curPos;
                    while (Character.isDigit(peek())) {
                        nextChar();
                    }
                    if (peek() == '.') {
                        nextChar();
                        if (!Character.isDigit(peek())) {
                            abort("Illegal character in number.");
                        }
                        while (Character.isDigit(peek())) {
                            nextChar();
                        }
                    }
                    tokText = source.substring(startPos, curPos + 1);
                    token = new Token(tokText, TokenType.NUMBER);
                } else if (Character.isLetter(curChar)) {
                    startPos = curPos;
                    while (Character.isLetterOrDigit(peek())) {
                        nextChar();
                    }
                    tokText = source.substring(startPos, curPos + 1);
                    TokenType keyword = Token.checkIfKeyword(tokText);
                    if (keyword == null) {
                        token = new Token(tokText, TokenType.IDENT);
                    } else {
                        token = new Token(tokText, keyword);
                    }
                } else {
                    abort("Unknown token: " + curChar);
                }
                break;
        }

        nextChar();
        return token;
    }
}


