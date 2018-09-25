package interpreter;

import interpreter.bytecodes.ByteCode;
import java.util.Stack;
import java.util.List;
import java.util.Scanner;

public class VirtualMachine {

    protected Program program; //bytecode program
    protected int pc; //program counter
    protected RunTimeStack runStack; //runtime stack
    protected Stack<Integer> returnAddrs; //push-pop when call/return functions
    protected boolean isRunning; //true while VM is running
    protected ByteCode currBC; //current byteCode being executed
    protected boolean dump; //dump feature

    public VirtualMachine(Program program) {
        this.program = program;
        pc = 0;
        runStack = new RunTimeStack();
        returnAddrs = new Stack<Integer>();
        isRunning = false;
        dump = false;
    }

    public void executeProgram() {
        currBC = program.getCode(pc);
        isRunning = true;
        while (isRunning) {
            currBC.execute(this);
            if (dump) {
                String output = currBC.getDebugLine(this);
                if (output != null) {
                    System.out.println(output);

                }
                runStack.dump();
                System.out.println();

            }
            if (isRunning) {
                currBC = program.getCode(pc);
            }
        }
    }

    public void stopRunning() {
        isRunning = false;
    }

    public int getProgramCounter() {
        return pc;
    }

    public void setProgramCounter(int addr) {
        isRunning = true;
        pc = addr;
    }

    /*
    * Remove last called return addrs
     */
    public int popReturnAddrs() {
        return returnAddrs.pop();
    }

    /*
    * Pushes new return addrs
     */
    public void pushReturnAddrs() {
        returnAddrs.push(pc);
    }

    public List<String> getArgs() {
        return runStack.getArgs();
    }

    public void newFrameAt(int offset) {

        runStack.newFrameAt(offset);
    }

    public int pushRunStack(int value) {
        runStack.push(value);
        return runStack.peek();
    }

    public int peekStack() {
        return runStack.peek();
    }

    public int popRunStack() {
        return runStack.pop();
    }

    public int storeRunStack(int offset) {
        return runStack.store(offset);
    }

    public int loadRunStack(int offset) {
        return runStack.load(offset);
    }

    /*
    * Read in integers for input
     */
    public int readInt() {
        int val = 0;
        Scanner in = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("Enter integer value:");
                String stringVal = in.next();
                val = Integer.parseInt(stringVal);
                break;
            } catch (Exception e) {
                System.exit(1);
            }
        }
        return val;
    }

    public void writeInt() {
        int val = 0;
        val = runStack.peek();
        System.out.println(val);
    }

    public void popRunStackFrame() {
        runStack.popFrame();
    }

    public int elementAt(int offset) {
        return this.runStack.elementAt(offset);
    }

    /* 
    * Turn dump on
     */
    public void dumpOn() {
        dump = true;
    }

    /* 
    * Turn dump off
     */
    public void dumpOff() {
        dump = false;
    }

    /*
    * Return size of runStack
     */
    public int runStackSize() {
        return runStack.size();
    }
}
