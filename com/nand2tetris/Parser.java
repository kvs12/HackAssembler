package com.nand2tetris;

import java.io.*;

public class Parser {
    private BufferedReader source;
    private File sourceFile;
    private String currentCommand = null;

    public Parser(File source) throws FileNotFoundException {
        this.source = new BufferedReader(new FileReader(source));
        this.sourceFile = source;
    }

    public void reset() throws FileNotFoundException {
        this.source = new BufferedReader(new FileReader(sourceFile));
    }


    /**
     * Reads the next command from
     * the input and makes it the current
     * command. Should be called only
     * if hasMoreCommands() is true.
     * Initially there is no current command.
     */
    public void advance() {
        currentCommand = readNextCommand();
    }

    public String symbol() {
        if (commandType().equals(CommandType.A_COMMAND)) {
            return currentCommand.substring(1);
        } else if (commandType().equals(CommandType.L_COMMAND)) {
            return currentCommand.substring(1, currentCommand.length() - 1);
        }
        return null;
    }

    public String dest() {
        if (commandType().equals(CommandType.C_COMMAND)
                && currentCommand.contains("=")) {
            return currentCommand.substring(0, currentCommand.indexOf("="));
        }
        return null;
    }

    public String comp() {
        if (commandType().equals(CommandType.C_COMMAND)) {
            String comp = currentCommand;
            String dest = dest();
            String jump = jump();
            if (dest != null) {
                // comp is right after dest part
                comp = comp.substring(dest.length() + 1);
            } else if (jump != null) {
                // comp is -1 (';') before jump part
                int jumpIndx = currentCommand.indexOf(jump);
                comp = comp.substring(0, jumpIndx - 1);
            }
            return comp;
        }
        return null;
    }

    public String jump() {
        if (commandType().equals(CommandType.C_COMMAND)
                && currentCommand.contains(";")) {
            return currentCommand
                    .substring(currentCommand.indexOf(';') + 1,
                            currentCommand.length());
        }
        return null;
    }

    /**
     *
     * @return
     */
    private String readNextCommand() {
        try {
            String command = source.readLine().replaceAll("\\s", "");
            if (command.contains("//")) {
                int commentStart = command.indexOf("//");
                command = command.substring(0, commentStart).trim();
            }
            return command;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public CommandType commandType() {
        if (currentCommand.startsWith("@")) {
            return CommandType.A_COMMAND;
        } else if (currentCommand.startsWith("(")) {
            return CommandType.L_COMMAND;
        } else {
            return CommandType.C_COMMAND;
        }
    }

    /**
     * Are there more commands in the input?
     * @return
     */
    public boolean hasMoreCommands() {
        try {
            int lineLength = 80;
            source.mark(lineLength);
            String line;
            while ((line = source.readLine()) != null) {
                if (!line.isEmpty() && !line.trim().startsWith("//")) {
                    // Command is found, reset buffer
                    source.reset();
                    return true;
                }
                // Invoke mark before next line reading
                source.mark(lineLength);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
