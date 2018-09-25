package interpreter.debugger.ui;

import interpreter.Program;
import interpreter.debugger.DebugVM;
import interpreter.debugger.SourceLineEntry;
import interpreter.debugger.FunctionEnvironmentRecord;
import interpreter.debugger.DebugSymTable;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

public class DebugUI {

    DebugVM debugVM;

    public DebugUI(Program program, String sourceCode, Set<Integer> breakpointSet) {
        debugVM = new DebugVM(program, sourceCode, breakpointSet);
    }

    /*
    * Called by the interpreter when in debug mode to start the debugger virtual machine
    */
    public void run() {
        String input = "";
        Scanner keyboard = new Scanner(System.in);
        displayFunctionSource(); //initial function source
        System.out.println("Type ? for help");
        do {//display prompt while input is not q
            this.displayPrompt();
            input = keyboard.nextLine();

            String sepInput[] = input.split("\\s"); //seperated input
            if (!(sepInput[0].equals("q"))) {
                processInput(sepInput);
            }
        } while (!input.equals("q"));
        System.out.println("******Execution Halted*******");
        System.exit(1);
    }

    private void displayPrompt() {//display prompt, prints everytime user needs to issue a command
        System.out.print("> ");
    }

    private void displayFunctionSource() {
        Vector<SourceLineEntry> currentFunctionSource = debugVM.getCurrentFunctionSource();
        int curLineIndex = debugVM.getCurrentExecutionLine();

        int startLine = debugVM.getCurrentFunctionStartLine();
        int stopLine = debugVM.getCurrentFunctionStopLine();

        curLineIndex = curLineIndex - startLine;

        for (int i = 0; i < currentFunctionSource.size(); i++) { //output function source line by line
            String toOutput = "";
            SourceLineEntry currEntry = currentFunctionSource.get(i);

            if (currEntry.isBreakPointSet()) {//check if breakpoint is set on currentline
                toOutput += " *";
            } else {
                toOutput += "  ";
            }
            toOutput += String.format(" %3d.", (i + startLine));

            toOutput += " " + String.format("%-40s", currEntry.getSourceLine());

            if (i == curLineIndex) { //check if current line on the same line as the line to output
                toOutput += String.format("%-3s", "  <--------");
            } else {
                toOutput += String.format("%-3s", "       ");
            }
            System.out.println(toOutput);
        }
        if(debugVM.getCurrentExecutionLine() == stopLine && debugVM.getCurrentExecutionLine() != -1){ //once execution line reaches the end line of the source, exit debug VM
            System.out.println("******Execution Halted*******");
            System.exit(1);
        }
    }

    private void processInput(String[] input) {
        String command = "";
        command = input[0];

        if (command.equalsIgnoreCase("q"))//quit
        {
            ;
        } else if ((command.equalsIgnoreCase("ov")))//step over
        {
            debugVM.setStepOver();
            if (debugVM.debugExecuteProgram()) {
                this.displayFunctionSource();
            }
        } else if ((command.equalsIgnoreCase("in"))) {
            debugVM.setStepIn();
            if (debugVM.debugExecuteProgram()) {
                this.displayFunctionSource();
            }
        } else if ((command.equalsIgnoreCase("out"))) //step out
        {
            debugVM.setStepOut();

            if (debugVM.debugExecuteProgram()) {
                this.displayFunctionSource();
            }
        } else if ((command.equalsIgnoreCase("bp"))) { //set breakpointSet
            try {
                for (int i = 1; i < input.length; i++) {
                    int breakLine;
                    breakLine = Integer.valueOf(input[i]);
                    if (debugVM.setBp(breakLine)) {
                        System.out.println("Breakpoints Set: " + breakLine);
                    } else {
                        System.out.println("Cannot set breakpoint at line: " + breakLine);
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Error: Invalid breakpoint");
            }
        } else if ((command.equalsIgnoreCase("cl"))) { //clear breakpointSet
            try {
                if (input.length == 1) {
                    debugVM.clearAllBreakpoints();
                    System.out.println("Breakpoints cleared");
                } else if (input.length > 1) {
                    for (int i = 1; i < input.length; i++) {
                        int breakLine;
                        breakLine = Integer.valueOf(input[i]);
                        if (debugVM.unsetBp(breakLine)) {
                            System.out.println("Breakpoint removed at line: " + breakLine);
                        } else {
                            System.out.println("Can't remove breakpoint at line: " + breakLine);
                        }
                    }
                }
            } catch (Exception e) {
            }
        } else if ((command.equalsIgnoreCase("v"))) {//display function variables
            displayFunction();
        } else if ((command.equalsIgnoreCase("s"))) {//display source code
            this.displayFunctionSource();
        } else if ((command.equalsIgnoreCase("c"))) {//continue execution
            if (debugVM.debugExecuteProgram()) {
                this.displayFunctionSource();
            }
        } else if ((command.equalsIgnoreCase("li"))) {//list breakpointSet
            listBreakPoints();
        } else if ((command.equalsIgnoreCase("ft"))) {//function trace
            try {
                if (input.length == 2) {
                    String flag = input[1];
                    if (flag.equals("on")) {
                        setTrace(true);
                        System.out.println("Function trace on");
                    } else if (flag.equals("off")) {
                        setTrace(false);
                        System.out.println("Function trace off");
                    } else {
                        System.out.println(
                                "Error: Use 'ft on/off'");
                    }
                } else {
                    System.out.println(
                            "Error: Use 'ft on/off'");
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Error: Invalid breakpoint");
            }
        } else if ((command.equalsIgnoreCase("pc"))) {//print call stack
            printCallStack();
        } else if ((command.equalsIgnoreCase("help")) || (command.equalsIgnoreCase("?"))) {
            this.printHelp();
        } else {
            System.out.println("Invalid command; " + "type '?' to get a list of avaliable commands.");
        }
    }
    
    /*
    * Help command initiated by ? or help
    */
    private void printHelp() {
        System.out.printf("%1$-10s %2$-10s\n", "? or help", "Displays command list");
        System.out.printf("%1$-10s %2$-10s\n", "ft", "Function Trace, use 'ft on/off' to set trace on or off");
        System.out.printf("%1$-10s %2$-10s\n", "c", "Continues execution until the next breakpoint");
        System.out.printf("%1$-10s %2$-10s\n", "bp n", "Sets breakpoint(s). For setting multiple lines seperate line# args by space. i.e 'bp 2 3'");
        System.out.printf("%1$-10s %2$-10s\n", "cl n", "Clears breakpoint(s). For clearing multiple lines seperate line# args by space, i.e 'cl 2 3'");
        System.out.printf("****************Step****************\n");
        System.out.printf("%1$-10s %2$-10s\n", "out", "Step Out");
        System.out.printf("%1$-10s %2$-10s\n", "in", "Step In");
        System.out.printf("%1$-10s %2$-10s\n", "ov", "Step Over");
        System.out.printf("***************Display***************\n");
        System.out.printf("%1$-10s %2$-10s\n", "s", "Displays the source code");
        System.out.printf("%1$-10s %2$-10s\n", "v", "Displays current variables");
        System.out.printf("%1$-10s %2$-10s\n", "li", "List breakpointSet. Breakpoints listed are seperated by a space.");
        System.out.printf("%1$-10s %2$-10s\n", "pc", "Print call stack");
        System.out.printf("%1$-10s %2$-10s\n", "q", "Quits execution and exits the debugger");
    }

    /*
    * Print out local function variables
     */
    public void displayFunction() {
        FunctionEnvironmentRecord currRecord;
        currRecord = debugVM.getCurrentEnvironmentRecord();

        DebugSymTable symTable = currRecord.getSymbolTable();

        Set<String> keys = symTable.keys();
        if (keys.isEmpty()) {
            System.out.println("No Local Variables");
        }
        for (String e : keys) {
            Object obj = symTable.get(e);
            int offset = (int) obj;
            int value = debugVM.elementAt(offset);
            System.out.println(e + " : " + value);
        }
    }

    /*
    * List all of the currently set breakpointSet
    */
    private void listBreakPoints() {
        List<Integer> breaks = debugVM.getBreakPoints();
        for (int i : breaks) {
            System.out.println("Breakpoints: " + i);
        }
    }
    
    /*
    Prints call stack in order of active function down to  main function
    */
    private void printCallStack() {
        List<FunctionEnvironmentRecord> records = debugVM.getEnvironmentRecordList();
        FunctionEnvironmentRecord e;
        for (int i = (records.size() - 1); i >= 0; i--) {
            e = records.get(i);
            if (!(e.getFunctionName().equals("-"))) {
                System.out.println(e.getFunctionName() + ": " + e.getCurrentLine());
            }
        }
    }

    private void setTrace(boolean flag) { //set trace on or off
        if (flag) {
            debugVM.setTrace(true);
        } else {
            debugVM.setTrace(false);
        }
    }
}
