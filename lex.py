class Lexer:
    def __init__(self, source):
        self.source = source + '\n'
        self.curChar = ''
        self.curPos = -1
        self.nextChar()

    #Process the next character.
    def nextChar(self):
        self.curPos += 1
        if self.curPos >= len(self.source):
            self.curChar = '\0' #EOF
        else:
            self.curChar = self.source[self.curPos]

    #Return the lookahead character
    def peek(self):
        if self.curPos + 1 >= len(self.source):
            return '\0'
        return self.source[self.curPos+1]

    #Invalid token found, then print error message and exit.
    def abot(self, message):
        pass

    # Skip whitespace except newlines, which we will use to indicate the end of stmt.
    def skipWhitespace(self):
        pass

    # Skip comments in the code.
    def skipComent(self):
        pass

    # Return the next token.
    def getToken(self):
        pass