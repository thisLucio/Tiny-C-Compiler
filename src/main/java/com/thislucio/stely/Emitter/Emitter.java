package com.thislucio.stely.Emitter;

import java.io.FileWriter;
import java.io.IOException;

public class Emitter {
    private String fullPath;
    private StringBuilder header;
    private StringBuilder code;

    public Emitter(String fullPath) {
        this.fullPath = fullPath;
        this.header = new StringBuilder();
        this.code = new StringBuilder();
    }

    public void emit(String code) {
        this.code.append(code);
    }

    public void emitLine(String code) {
        this.code.append(code).append("\n");
    }

    public void headerLine(String code) {
        this.header.append(code).append("\n");
    }

    public void writeFile() {
        try (FileWriter writer = new FileWriter(fullPath)) {
            writer.write(header.toString() + code.toString());
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}