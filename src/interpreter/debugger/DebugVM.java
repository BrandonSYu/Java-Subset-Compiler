package interpreter.debugger;

import interpreter.Program;
import interpreter.VirtualMachine;
import interpreter.bytecodes.ByteCode;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

public class DebugVM extends VirtualMachine {

    private Vector<SourceLineEntry> sourceCodeLines;
    private Stack<FunctionEnvironmentRecord> environmentStack; //debug function environment records stored into stack
    private Set<Integer> breakpointSet; //set of valid breakpoints
    private FunctionEnvironmentRecord currRecord;//current environment record
    private BufferedReader source;
    private boolean inFunction = false; //entered function in debugger
    private boolean exit = false; //check if program is terminated
    private boolean stepOutCheck = false; //check if stepout
    private boolean stepInCheck = false; //check if stepin
    private boolean stepOverCheck = false; //check if stepover
    private boolean traceCheck = false; //check if trace is on
    private boolean toBreak = false;
    private int recordSize = 0; //debug function environment record size
    private int watch = 0; //watch line

    public DebugVM(Program program, String sourceCodeFileName, Set<Integer> validBreakPoints) {
        super(program);
        try {
            sourceCodeLines = new Vector<SourceLineEntry>();
            environmentStack = new Stack<FunctionEnvironmentRecord>();
            this.breakpointSet = validBreakPoints;
            source = new BufferedReader(new FileReader(sourceCodeFileName));
            String line;
            do {
                line = source.readLine();

                if (line != null) {
                    SourceLineEntry currEntry = new SourceLineEntry(line, false);
                    sourceCodeLines.add(currEntry);
                }

            } while (line != null);
            FunctionEnvironmentRecord firstRecord = new FunctionEnvironmentRecord(); //create first record
            firstRecord.setStartLine(1);
            firstRecord.setEndLine(sourceCodeLines.size());
            environmentStack.push(firstRecord);

        } catch (IOException ex) {
            System.out.println("***IOException can't read from file: " + sourceCodeFileName);
            System.exit(1);
        }
    }

    /*
    * Check if program has already been executed
    */
    public boolean debugExecuteProgram() {
        if (!exit) {
            this.executeProgram();
            return true;
        } else {
            return false;
        }
    }

    public void createNewEnvironmentRecord() {
        FunctionEnvironmentRecord currEnv = new FunctionEnvironmentRecord();
        environmentStack.push(currEnv);
    }

    public void setJustEnteredFunction() {
        this.inFunction = true;
    }

    @Override
    public void stopRunning() {
        super.stopRunning();
        exit = true;
        FunctionEnvironmentRecord currRecord = this.getCurrentEnvironmentRecord();
        currRecord.setCurrentLine(currRecord.getEndLine());
        System.out.println("******Execution Halted*******");
    }

    public void unsetFunc() {//unset intrinsic function
        this.inFunction = false;
    }

    public void setFunction(String funcName, int startLine, int stopLine) {
        if (!environmentStack.empty()) {
            environmentStack.peek().setFunction(funcName, startLine, stopLine);
        }

        unsetFunc();

        if (!runStack.isEmpty() && traceCheck) {
            printTrace(funcName, runStack.peek());
        }
    }

    public FunctionEnvironmentRecord getCurrentEnvironmentRecord() {
        return this.environmentStack.peek();
    }

    public List<FunctionEnvironmentRecord> getEnvironmentRecordList() {
        List<FunctionEnvironmentRecord> records = new ArrayList();

        for (FunctionEnvironmentRecord e : environmentStack) {
            records.add(e);
        }

        return records;
    }

    public void setCurrentLine(int currentLine) {
        if (!environmentStack.empty()) {
            environmentStack.peek().setCurrentLine(currentLine);
        }

        if (!inFunction) {
            if (!(currentLine < 1)) {
                if (sourceCodeLines.get(currentLine - 1).isBreakptSet) {
                    super.stopRunning();
                }
            }
        }

    }

    public void popSymbolTable(int n) {
        if (!environmentStack.empty()) {
            environmentStack.peek().pop(n);
        }
    }

    public void popEnvironmentRecord() {
        if (!environmentStack.empty()) {
            environmentStack.pop();
        }
    }

    /*
    * Gets current function, checks for intrinsic function
    */
    public Vector<SourceLineEntry> getCurrentFunctionSource() {

        Vector<SourceLineEntry> currFunctionEntries;

        if (environmentStack.empty()) {
            currFunctionEntries = sourceCodeLines;
        } else {
            FunctionEnvironmentRecord currEnvRecord = environmentStack.peek();
            currFunctionEntries = new Vector<SourceLineEntry>();

            if (currEnvRecord.getStartLine() == -1) {//startline -1 for intrinsic
                String intrinsicOutput = ("Entered an intrinsic function: ");
                intrinsicOutput += currEnvRecord.getFunctionName();
                SourceLineEntry intrinsicEntry = new SourceLineEntry(intrinsicOutput, false);
                currFunctionEntries.add(intrinsicEntry);
            } else {

                int startLine = 0;
                int stopLine = 0;

                startLine = currEnvRecord.getStartLine();
                stopLine = currEnvRecord.getEndLine();

                for (int i = (startLine - 1); i < stopLine; i++) {
                    currFunctionEntries.add(sourceCodeLines.get(i));
                }
            }
        }
        return currFunctionEntries;
    }

    @Override
    public void executeProgram() {
        try {
            currBC = program.getCode(pc);
            isRunning = true;
            while (isRunning) {
                if (stepOutCheck) {
                    if (recordSize > environmentStack.size()) {
                        stepOutCheck = false;
                        toBreak = true;
                    }                  
                } 
                else if (stepInCheck) {//check watchline and size of environmentStack
                    if ((recordSize < environmentStack.size())|| ((watch != environmentStack.peek().getCurrentLine())
                            && (recordSize == environmentStack.size()))) {
                        stepInCheck = false;
                        toBreak = true; 
                    }                    
                } 
                else if (stepOverCheck) {//check watchline and size of environmentStack
                    if ((watch != environmentStack.peek().getCurrentLine())&& ((recordSize > environmentStack.size())
                            || (recordSize == environmentStack.size()))) {
                        stepOverCheck = false;
                        toBreak = true;
                    }
                }

                if (toBreak) {//if toBreak is set true due to step methods
                    if (!this.inFunction) { //check if intrinsic function
                        if (!formalCheck(currBC)) { //check if current bytecode is formal
                            toBreak = false; 
                            isRunning = false;
                            break; //break out of execution
                        }
                    }
                }
                
                pc++;//increment program counter
                currBC.execute(this);//execute current bytecode
                if (dump) {
                    String debugOutput = currBC.getDebugLine(this);
                    if (debugOutput != null) {
                        System.out.println(debugOutput);
                    }
                    runStack.dump();
                    System.out.println();
                }
                if (isRunning) {
                    currBC = program.getCode(pc);
                }
            }
        } catch (Exception e) {}
    }

    public int getCurrentExecutionLine() {
        if (environmentStack.empty()) {
            return 0;
        } else {
            FunctionEnvironmentRecord currEnvRecord = environmentStack.peek();
            return currEnvRecord.getCurrentLine();
        }
    }

    public int getCurrentFunctionStartLine() {

        FunctionEnvironmentRecord currEnvRecord = environmentStack.peek();

        return currEnvRecord.getStartLine();

    }

    public int getCurrentFunctionStopLine() {

        FunctionEnvironmentRecord currEnvRecord = environmentStack.peek();

        return currEnvRecord.getEndLine();

    }

    public void enterSymbolAtCurrentOffset(String sym) {
        if (!environmentStack.empty()) {
            int runStackPtr = this.runStack.getCurrentStackPtr();
            runStackPtr--;
            FunctionEnvironmentRecord currEnvRecord = environmentStack.peek();
            currEnvRecord.enter(sym, runStackPtr);
        }
    }

    public void enterSymbolAtOffset(String sym, int offset) {
        if (!environmentStack.empty()) {

            FunctionEnvironmentRecord currEnvRecord = environmentStack.peek();

            currEnvRecord.enter(sym, offset);
        }
    }

    public boolean setBp(int lineNum) {
        if (breakpointSet.contains(lineNum)) {
            sourceCodeLines.get(lineNum - 1).setBreakPoint();
            return true;
        } else {
            return false;
        }
    }

    public boolean unsetBp(int lineNum) {
        if (breakpointSet.contains(lineNum)) {
            sourceCodeLines.get(lineNum - 1).unsetBreakPoint();
            return true;
        } else {
            return false;
        }
    }

    public void clearAllBreakpoints() {
        Iterator<Integer> breaks = breakpointSet.iterator();

        while (breaks.hasNext()) {
            int i = breaks.next();
            if (i > 0) {
                sourceCodeLines.get(i - 1).unsetBreakPoint();
            }
        }
    }

    public void setStepOut() {
        stepOutCheck = true;
        recordSize = environmentStack.size();
    }

    public void setStepOver() {

        stepOverCheck = true;
        recordSize = environmentStack.size();
        watch = environmentStack.peek().getCurrentLine();
    }

    public void setStepIn() {
        stepInCheck = true;
        recordSize = environmentStack.size();
        watch = environmentStack.peek().getCurrentLine();
    }

    public void setTrace(boolean flag) {
        traceCheck = flag;
    }

    /*
    * Gets currently set breakpoints
    */
    public List<Integer> getBreakPoints() {
        List<Integer> breakLines = new ArrayList();

        for (int i = 0; i < sourceCodeLines.size(); i++) {
            if (sourceCodeLines.get(i).isBreakptSet) {
                breakLines.add(i + 1);
            }
        }
        return breakLines;
    }
    
    /*
    * Check if bytecode is a formal for intrinsic function
    */
    public boolean formalCheck(ByteCode code) {
        if (code.getByteCodeName().equals("DebugFormalCode")) {
            return true;
        } else {
            return false;
        }
    }

    private void printTrace(String funcName, int returnVal) {
        FunctionEnvironmentRecord currRecord;
        String output = "";
        String exit = "";
        int envStackSize = environmentStack.size();
        for (int i = 1; i < envStackSize - 1; i++) {//number of spaces for indentation equals the size of the call
            output += "  ";
            exit += " ";
        }

        if (!environmentStack.empty()) {
            currRecord = environmentStack.peek();

            String[] splitName = funcName.split("<");
            String func = splitName[0];

            if (currRecord.getFunctionName().equals("Read")) {//check if read function
                output += func + "()";
            } else {//print function name and return value otherwise              
                output += func + "(" + returnVal + ")"; 
            }
        }

        System.out.println(output);
    }
}
