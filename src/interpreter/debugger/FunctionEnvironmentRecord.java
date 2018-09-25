package interpreter.debugger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FunctionEnvironmentRecord {

    private DebugSymTable table;
    private int startLine, endLine, currentLine;
    private String name;

    public FunctionEnvironmentRecord() {//set initial environment record
        table = new DebugSymTable();
        currentLine = 1;
        startLine = 1;
    }

    public void enter(String id, int offset) {
        table.put(id, offset);
    }

    public DebugSymTable getSymbolTable() {
        return this.table;
    }

    public String getFunctionName() {
        String[] splitFuncName = name.split("<");

        return splitFuncName[0];
    }

    public void setFunction(String name, int start, int stop) {
        this.name = name;
        this.startLine = start;
        this.endLine = stop;
    }

    public void setStartLine(int line) {
        this.startLine = line;
    }

    public void setEndLine(int line) {
        this.endLine = line;
    }

    public void setCurrentLine(int line) {
        this.currentLine = line;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void pop(int n) {
        table.pop(n);
    }

    public void beginScope() {
        table.beginScope();
    }

    public int getStartLine() {
        return this.startLine;
    }

    public int getEndLine() {
        return this.endLine;
    }

    public int getCurrentLine() {
        return currentLine;
    }
}
