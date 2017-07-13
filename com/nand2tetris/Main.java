package com.nand2tetris;

import java.io.*;

public class Main {
    private static SymbolTable symbolTable = new SymbolTable();
    private static File source;

    // Walk through the file, pre-define symbolic table
    private static void performFirstPass(Parser parser) throws FileNotFoundException {
        // The initial rom address of an instruction
        int instrRomAddr = 0;
        while (parser.hasMoreCommands()) {
            parser.advance();
            CommandType commandType = parser.commandType();
            if (commandType.equals(CommandType.C_COMMAND)
                    || commandType.equals(CommandType.A_COMMAND)) {
                ++instrRomAddr;
            }
            if (commandType.equals(CommandType.L_COMMAND)) {
                // Associate label with the address of the instruction
                symbolTable.addEntry(parser.symbol(), instrRomAddr);
            }
        }
    }

    private static void performSecondPass(Parser parser, File outFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFile))) {
            Code code = new Code();
            String lineToWrite = "";
            // Starting memory address for new variables
            int varStartOffset = 0x0010;
            while (parser.hasMoreCommands()) {
                parser.advance();
                CommandType commandType = parser.commandType();
                if (commandType.equals(CommandType.C_COMMAND)) {
                    // Get C command representation based on provided parts
                    lineToWrite = code.getCCommandRepr(
                            parser.comp(),
                            parser.dest(),
                            parser.jump()
                    );
                }
                if (commandType.equals(CommandType.A_COMMAND)) {
                    // Get A command representation based on symbol
                    String symbol = parser.symbol();
                    // Check if it's pre-defined symbol or a new variable
                    if (Character.isLetter(symbol.charAt(0))) {
                        if (symbolTable.contains(symbol)) {
                            symbol = String.valueOf(symbolTable.getAddress(symbol));
                        } else {
                            symbolTable.addEntry(symbol, varStartOffset);
                            symbol = String.valueOf(varStartOffset);
                            varStartOffset++;
                        }
                    }
                    lineToWrite = code.getACommandRepr(symbol);
                }
                if (commandType.equals(CommandType.L_COMMAND)) {
                    continue;
                }
                writer.write(lineToWrite);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 1 || !args[0].endsWith(".asm")) {
            printUsage();
            System.exit(1);
        }
        source = new File(args[0]);
        if (!source.exists()) {
            System.out.println("Source file " + source.getName() + " doesn't exist");
            System.exit(1);
        }
        File outFile = new File(getOutputPath());
        Parser parser = new Parser(source);
        performFirstPass(parser);
        parser.reset();
        performSecondPass(parser, outFile);
    }

    private static String getOutputPath() {
        final String sourceFilename = source.getName();
        final String hackFilename = sourceFilename.substring(0, sourceFilename.lastIndexOf('.')) + ".hack";
        return source.getPath().replace(sourceFilename, hackFilename);
    }

    private static void printUsage() {
        System.out.println("Provide .asm source file as a first argument to the assembler");
    }
}
