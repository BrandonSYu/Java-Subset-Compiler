package interpreter;

import interpreter.debugger.DebugByteCodeLoader;
import interpreter.debugger.DebugCodeTable;
import interpreter.debugger.ui.DebugUI;
import java.io.*;
import java.util.Set;

/**
 * <pre>
 *
 *
 *
 *     Interpreter class runs the interpreter:
 *     1. Perform all initializations
 *     2. Load the bytecodes from file
 *     3. Run the virtual machine
 *
 *
 *
 * </pre>
 */
public class Interpreter {

    private ByteCodeLoader bcl;

    public Interpreter(String codeFile) {
        try {
            CodeTable.init(); //Initialize the CodeTable            
            bcl = new ByteCodeLoader(codeFile);
        } catch (IOException e) {
            System.out.println("**** " + e);
        }
    }

    void run() {
        Program program = bcl.loadCodes();
        VirtualMachine vm = new VirtualMachine(program);
        vm.executeProgram();
    }
    
    /*
    * Checks for bytecode filename and sourcecode file name 
    * Determines whether or not in debug mode and loads respective CodeTable, ByteCodeLoader, and VM
    */
    public static void main(String[] args) {
        boolean debugMode = false;
        String byteCodeFileName = "";
        String sourceCodeFileName = "";

        if (args.length == 0) {
            System.out.println("***Incorrect usage, try: java -jar interpreter.jar <file>");
            System.exit(1);
        }
        if (args[0].equals("-d")) { //debug mode
            try {
                byteCodeFileName = args[1] + ".x.cod";
                sourceCodeFileName = args[1] + ".x";
                CodeTable.init();
                DebugCodeTable.init();
                DebugByteCodeLoader bcl = new DebugByteCodeLoader(byteCodeFileName);
                Program program = bcl.loadCodes();

                Set<Integer> validBreakPoints = bcl.getValidBreakpointLines();
                System.out.printf("*****Debugging " + args[1] + "*****\n");
                new DebugUI(program, sourceCodeFileName, validBreakPoints).run();

            } catch (Exception e) {
                System.exit(1);
            }
        } else {
            byteCodeFileName = args[0];
            (new Interpreter(byteCodeFileName)).run();
        }
    }
}
