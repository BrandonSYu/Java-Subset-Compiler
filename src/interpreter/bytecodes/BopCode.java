package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.Vector;

public class BopCode extends ByteCode {

    String operator;

    public BopCode() {
        super("BopCode");
    }

    @Override
    public void execute(VirtualMachine vm) {
        int topInt, lowerInt;
        lowerInt = vm.popRunStack();
        topInt = vm.popRunStack();
        String oper = this.operator;
        int returnVal = 0;
        switch (oper) {
            case "+":
                returnVal = topInt + lowerInt;
                break;
            case "-":
                returnVal = topInt - lowerInt;
                break;
            case "/":
                returnVal = topInt / lowerInt;
                break;
            case "*":
                returnVal = topInt * lowerInt;
                break;
            case "==":
                returnVal = (topInt == lowerInt) ? 1 : 0;
                break;
            case "!=":
                returnVal = (topInt != lowerInt) ? 1 : 0;
                break;
            case "<=":
                returnVal = (topInt <= lowerInt) ? 1 : 0;
                break;
            case ">":
                returnVal = (topInt > lowerInt) ? 1 : 0;
                break;
            case ">=":
                returnVal = (topInt >= lowerInt) ? 1 : 0;
                break;
            case "<":
                returnVal = (topInt < lowerInt) ? 1 : 0;
                break;
            case "|":
                if (topInt == 0 && lowerInt == 0) {
                    returnVal = 0;
                } else {
                    returnVal = 1;
                }
                break;
            case "&":
                if (topInt == 1 && lowerInt == 1) {
                    returnVal = 1;
                } else {
                    returnVal = 0;
                }
                break;
        }
        vm.pushRunStack(returnVal);
    }

    @Override
    public String getDebugLine(VirtualMachine vm) {
        return this.getSourceByteCode();
    }

    @Override
    public void init(Vector<String> args) {
        if (!args.isEmpty()) {
            this.operator = args.get(0);

        }
    }
}
