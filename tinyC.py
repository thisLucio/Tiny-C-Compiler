import sys
from email.parser import Parser

from lex import *
from parse import *

def main():
    print("Stely Compiler BETA RELEASE v0.0.1")

    if len(sys.argv) != 2:
        sys.exit("Err> The Compiler needs source file as args")
    with open(sys.argv[1]. 'r') as inputFile:
        source = inputFile.read()

        lexer = Lexer(source)
        parser = Parser(lexer)

        parser.program()
        print("Parsing completed.")


main()