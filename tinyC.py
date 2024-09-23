from lex import *

def main():
    #source = "LET foobar = 123"
    source = "+- */"
    lexer = Lexer(source)

    token = lexer.getToken()
    while token.kind != TokenType.EOF:
        print(token.kind)
        token = lexer.getToken()

main()