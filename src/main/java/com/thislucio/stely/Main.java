package com.thislucio.stely;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {

        System.out.println("Stely Compiler v0.0.0.1");
        String sourceCode = readFromFile(args);



    }

    public static String readFromFile(String[] args){
        if (args.length != 1) {
            System.err.println("Error: Compiler needs source file as argument.");
            System.exit(1); // Exit with an error code
        }

        // Read the file
        String source = new String();
        try {
            source = Files.readString(Paths.get(args[0]));
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1);
            return source;
        }
        return source;
    }

}