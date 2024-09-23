import sys
from lex import *
from parse import *
from emit import *

def main():
    print("Stely Compiler BETA RELEASE v0.0.1")

    if len(sys.argv) != 2:
        sys.exit("Err> The Compiler needs source file as args")
    with open(sys.argv[1], 'r') as inputFile:
        source = inputFile.read()

        lexer = Lexer(source)
        emitter = Emitter("out.c")
        parser = Parser(lexer, emitter)

        parser.program()
        emitter.writeFile()
        print("Parsing completed.")


main()