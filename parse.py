import sys
from lex import *

class Parser:
    def __init__(self,lexer):
        self.lexer = lexer

        self.curToken = None
        self.peekToken = None
        self.nextToken()
        self.nextToken() # Call this twice to initialize current and peek.

    def checkToken(self, kind):
        return  kind == self.curToken.kind

    def checkPeek(self, kind):
        return kind == self.peekToken.kind

    def match(self, kind):
        if not self.checkToken(kind):
            self.abort("Stely Expected " + kind.name + ", got " + self.curToken.kind.name)
        self.nextToken()

    def nextToken(self):
        self.curToken = self.peekToken
        self.peekToken = self.lexer.getToken()

    def abort(self, message):
        sys.exit("Err> " + message)

    # Production rules.

    # program ::= {statement}
    def program(self):
        print("PROGRAM")

        # Parse all the stmt in the program.
        while not self.checkToken(TokenType.EOF):
            self.statement()

    # One of the following stmt's...
    def statement(self):
        # Check the first tokent to see what kind of stmt this is.

        # "PRINT" (expression | string)
        if self.checkToken(TokenType.PRINT):
            print("STATEMENT-PRINT")
            self.nextToken()

            if self.checkToken(TokenType.STRING):
                # Simple string.
                self.nextToken()
            else:
                # Expect an expression.
                self.expression()
        # "IF" comparison "THEN" {statement} "ENDIF"
        elif self.checkToken(TokenType.IF):


        # Newline.
        self.nl()

    # nl ::= '\n'+
    def nl(self):
         print("NEWLINE")

         # Require at least one newline.
         self.match(TokenType.NEWLINE)
         # But we will allow extra newlines too, of course.
         while self.checkToken(TokenType.NEWLINE):
            self.nextToken()