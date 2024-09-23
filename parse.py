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
