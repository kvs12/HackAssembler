package com.nand2tetris;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by usename on 23.04.17.
 */
public class Code {
    private static Map<String, String> destMap = new HashMap<>(8);
    private static Map<String, String> jumpMap = new HashMap<>(8);
    private static Map<String, String> compMap = new HashMap<>();
    private static final String WORD = "0000000000000000";
    private static final int WORD_LENGTH = WORD.length();
    // the format is 111a cccc ccdd djjj
    private static final String INITIAL_C_COMMAND = "1110000000000000";

    static {
        destMap.put("NULL", "000");
        destMap.put("M", "001");
        destMap.put("D", "010");
        destMap.put("MD", "011");
        destMap.put("A", "100");
        destMap.put("AM", "101");
        destMap.put("AD", "110");
        destMap.put("AMD", "111");

        jumpMap.put("NULL", "000");
        jumpMap.put("JGT", "001");
        jumpMap.put("JEQ", "010");
        jumpMap.put("JGE", "011");
        jumpMap.put("JLT", "100");
        jumpMap.put("JNE", "101");
        jumpMap.put("JLE", "110");
        jumpMap.put("JMP", "111");

        compMap.put("0", "101010");
        compMap.put("1", "111111");
        compMap.put("-1", "111010");
        compMap.put("D", "001100");
        compMap.put("A", "110000");
        compMap.put("M", "110000");
        compMap.put("!D", "001101");
        compMap.put("!A", "110001");
        compMap.put("!M", "110001");
        compMap.put("-D", "110000");
        compMap.put("-A", "001111");
        compMap.put("-M", "001111");
        compMap.put("D+1", "011111");
        compMap.put("A+1", "110111");
        compMap.put("M+1", "110111");
        compMap.put("D-1", "001110");
        compMap.put("A-1", "110010");
        compMap.put("D+A", "000010");
        compMap.put("D-A", "010011");
        compMap.put("A-D", "000111");
        compMap.put("D&A", "000000");
        compMap.put("D|A", "010101");
        compMap.put("M-1", "110010");
        compMap.put("D+M", "000010");
        compMap.put("D-M", "010011");
        compMap.put("M-D", "000111");
        compMap.put("D&M", "000000");
        compMap.put("D|M", "010101");
    }
    
    public Code() {

    }

    public String getCCommandRepr(String compMnemonic,
                                  String destMnemonic,
                                  String jumpMnemonic) {
        StringBuilder stringBuilder = new StringBuilder(INITIAL_C_COMMAND);
        if (compMnemonic.contains("M")) {
            // M register is involved, a-bit is 1
            stringBuilder.replace(3, 4, "1");
        }
        stringBuilder.replace(4, WORD_LENGTH,
                comp(compMnemonic)
                + dest(destMnemonic)
                + jump(jumpMnemonic));
        return stringBuilder.toString();
    }

    private String dest(String mnem) {
        final String mnenUpper = String.valueOf(mnem).toUpperCase();
        if (destMap.containsKey(mnenUpper)) {
            return destMap.get(mnenUpper);
        } else {
            throw new RuntimeException("Unknown mnemonic for dest: " + mnem);
        }
    }

    private String comp(String mnem) {
        final String mnenUpper = mnem.toUpperCase();
        if (compMap.containsKey(mnenUpper)) {
            return compMap.get(mnenUpper);
        } else {
            throw new RuntimeException("Unknown mnemonic for comp: " + mnem);
        }
    }

    private String jump(String mnem) {
        final String mnenUpper = String.valueOf(mnem).toUpperCase();
        if (jumpMap.containsKey(mnenUpper)) {
            return jumpMap.get(mnenUpper);
        } else {
            throw new RuntimeException("Unknown mnemonic for jump: " + mnem);
        }
    }


    public String getACommandRepr(String symbol) {
        String binaryStr;
        StringBuilder stringBuilder = new StringBuilder(WORD);
        try {
            binaryStr = Integer.toBinaryString(Integer.parseInt(symbol));
        } catch (NumberFormatException ex) {
            throw new RuntimeException("Invalid symbol: " + symbol + "\n");
        }
        stringBuilder.replace(WORD_LENGTH - binaryStr.length(),
                WORD_LENGTH,
                binaryStr);
        return stringBuilder.toString();
    }
}
