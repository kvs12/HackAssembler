package com.nand2tetris;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private Map<String, Integer> symbolicTable = new HashMap<>();

    public SymbolTable() {
        addPredefinedSymbols();
    }

    private void addPredefinedSymbols() {
        symbolicTable.put("SP", 0x0000);
        symbolicTable.put("LCL", 0x0001);
        symbolicTable.put("ARG", 0x0002);
        symbolicTable.put("THIS", 0x0003);
        symbolicTable.put("THAT", 0x0004);
        symbolicTable.put("SCREEN", 0x4000);
        symbolicTable.put("KBD", 0x6000);

        for (int i = 0; i <= 15; i++) {
            final String label = "R" + i;
            symbolicTable.put(label, i);
        }
    }

    public void addEntry(String symbol, int address) {
        symbolicTable.put(symbol, address);
    }

    public boolean contains(String symbol) {
        return symbolicTable.containsKey(symbol);
    }

    public int getAddress(String symbol) {
        return symbolicTable.get(symbol);
    }
}
