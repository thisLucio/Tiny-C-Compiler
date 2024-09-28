package com.thislucio.stely;

import com.thislucio.stely.Emitter.Emitter;
import com.thislucio.stely.Lexer.Lexer;
import com.thislucio.stely.Parser.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        System.out.println("Stely Compiler BETA RELEASE v0.0.1");

        // Verificar o número de argumentos passados.
        if (args.length != 1) {
            System.out.println("Error: Compiler needs source file as argument.");
            System.exit(1);
        }

        String source;
        try {
            // Lê o conteúdo do arquivo fornecido como argumento.
            source = new String(Files.readAllBytes(Paths.get(args[0])));
        } catch (IOException e) {
            System.out.println("Error: Unable to read source file.");
            e.printStackTrace();
            return;
        }

        // Inicializar o lexer, emitter e parser.
        Lexer lexer = new Lexer(source);
        Emitter emitter = new Emitter("out.c");
        Parser parser = new Parser(lexer, emitter);

        // Iniciar o parser.
        parser.program();

        // Escrever a saída no arquivo.
        emitter.writeFile();
        System.out.println("Compiling completed.");
    }
}
