package com.thislucio.stely.Parser;

import com.thislucio.stely.Emitter.Emitter;
import com.thislucio.stely.Lexer.Lexer;
import com.thislucio.stely.Lexer.Token;
import com.thislucio.stely.Lexer.TokenType;

import java.util.HashSet;
import java.util.Set;

public class Parser {
    private Lexer lexer;
    private Emitter emitter;
    private Set<String> symbols;
    private Set<String> labelsDeclared;
    private Set<String> labelsGotoed;

    private Token curToken;
    private Token peekToken;

    public Parser(Lexer lexer, Emitter emitter) {
        this.lexer = lexer;
        this.emitter = emitter;

        this.symbols = new HashSet<>();
        this.labelsDeclared = new HashSet<>();
        this.labelsGotoed = new HashSet<>();

        nextToken();
        nextToken(); // Call this twice to initialize current and peek.
    }

    public boolean checkToken(TokenType kind) {
        return kind == curToken.kind;
    }

    public boolean checkPeek(TokenType kind) {
        return kind == peekToken.kind;
    }

    public void match(TokenType kind) {
        if (!checkToken(kind)) {
            abort("Expected " + kind.name() + ", got " + curToken.kind.name());
        }
        nextToken();
    }

    public void nextToken() {
        curToken = peekToken;
        peekToken = lexer.getToken();
    }

    public void abort(String message) {
        System.out.println("Err> " + message);
        System.exit(1);
    }

    // Production rules.
    public void program() {
        emitter.headerLine("#include <stdio.h>");
        emitter.headerLine("int main(void){");

        while (checkToken(TokenType.NEWLINE)) {
            nextToken();
        }

        // Parse all the statements in the program.
        while (!checkToken(TokenType.EOF)) {
            statement();
        }

        emitter.emitLine("return 0;");
        emitter.emitLine("}");

        for (String label : labelsGotoed) {
            if (!labelsDeclared.contains(label)) {
                abort("Attempting to GOTO to undeclared label: " + label);
            }
        }
    }

    public void statement() {
        if (checkToken(TokenType.PRINT)) {
            nextToken();
            if (checkToken(TokenType.STRING)) {
                emitter.emitLine("printf(\"" + curToken.text + "\\n\");");
                nextToken();
            } else {
                emitter.emit("printf(\"%.2f\\n\", (float)(");
                expression();
                emitter.emitLine("));");
            }
        } else if (checkToken(TokenType.IF)) {
            nextToken();
            emitter.emit("if(");
            comparison();
            match(TokenType.THEN);
            nl();
            emitter.emitLine("){");

            while (!checkToken(TokenType.ENDIF)) {
                statement();
            }

            match(TokenType.ENDIF);
            emitter.emitLine("}");
        } else if (checkToken(TokenType.WHILE)) {
            nextToken();
            emitter.emit("while(");
            comparison();
            match(TokenType.REPEAT);
            nl();
            emitter.emitLine("){");

            while (!checkToken(TokenType.ENDWHILE)) {
                statement();
            }

            match(TokenType.ENDWHILE);
            emitter.emitLine("}");
        } else if (checkToken(TokenType.LABEL)) {
            nextToken();
            if (labelsDeclared.contains(curToken.text)) {
                abort("Label already exists: " + curToken.text);
            }
            labelsDeclared.add(curToken.text);
            emitter.emitLine(curToken.text + ":");
            match(TokenType.IDENT);
        } else if (checkToken(TokenType.GOTO)) {
            nextToken();
            labelsGotoed.add(curToken.text);
            emitter.emitLine("goto " + curToken.text + ";");
            match(TokenType.IDENT);
        } else if (checkToken(TokenType.LET)) {
            nextToken();
            if (!symbols.contains(curToken.text)) {
                symbols.add(curToken.text);
                emitter.headerLine("float " + curToken.text + ";");
            }
            emitter.emit(curToken.text + " = ");
            match(TokenType.IDENT);
            match(TokenType.EQ);
            expression();
            emitter.emitLine(";");
        } else if (checkToken(TokenType.INPUT)) {
            nextToken();
            if (!symbols.contains(curToken.text)) {
                symbols.add(curToken.text);
                emitter.headerLine("float " + curToken.text + ";");
            }
            emitter.emitLine("if(0 == scanf(\"%f\", &" + curToken.text + ")) {");
            emitter.emitLine(curToken.text + " = 0;");
            emitter.emitLine("}");
            match(TokenType.IDENT);
        } else {
            abort("Invalid statement at " + curToken.text + " (" + curToken.kind.name() + ")");
        }

        nl();
    }

    public void comparison() {
        expression();
        if (isComparisonOperator()) {
            emitter.emit(curToken.text);
            nextToken();
            expression();
        }

        while (isComparisonOperator()) {
            emitter.emit(curToken.text);
            nextToken();
            expression();
        }
    }

    public boolean isComparisonOperator() {
        return checkToken(TokenType.GT) || checkToken(TokenType.GTEQ) || checkToken(TokenType.LT) ||
                checkToken(TokenType.LTEQ) || checkToken(TokenType.EQEQ) || checkToken(TokenType.NOTEQ);
    }

    public void expression() {
        term();
        while (checkToken(TokenType.PLUS) || checkToken(TokenType.MINUS)) {
            emitter.emit(curToken.text);
            nextToken();
            term();
        }
    }

    public void term() {
        unary();
        while (checkToken(TokenType.ASTERISK) || checkToken(TokenType.SLASH)) {
            emitter.emit(curToken.text);
            nextToken();
            unary();
        }
    }

    public void unary() {
        if (checkToken(TokenType.PLUS) || checkToken(TokenType.MINUS)) {
            emitter.emit(curToken.text);
            nextToken();
        }
        primary();
    }

    public void primary() {
        if (checkToken(TokenType.NUMBER)) {
            emitter.emit(curToken.text);
            nextToken();
        } else if (checkToken(TokenType.IDENT)) {
            if (!symbols.contains(curToken.text)) {
                abort("Referencing variable before assignment: " + curToken.text);
            }
            emitter.emit(curToken.text);
            nextToken();
        } else {
            abort("Unexpected token at " + curToken.text);
        }
    }

    public void nl() {
        match(TokenType.NEWLINE);
        while (checkToken(TokenType.NEWLINE)) {
            nextToken();
        }
    }
}
