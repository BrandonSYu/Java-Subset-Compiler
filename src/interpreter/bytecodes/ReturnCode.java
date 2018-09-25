package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.Vector;

public class ReturnCode extends ByteCode {

    private String comment;
    private int numArgs;

    public ReturnCode() {
        super("ReturnCode");
    }

    @Override
    public void init(Vector<String> args) {
        if (!args.isEmpty()) {
            this.comment = args.get(0);
            this.numArgs = args.size();
        }
    }

    @Override
    public void execute(VirtualMachine vm) {

        vm.popRunStackFrame();

        int returnAddr = vm.popReturnAddrs();

        vm.setProgramCounter(returnAddr);
    }

    @Override
    public String getDebugLine(VirtualMachine vm) {
        if (this.numArgs == 0) {
            return this.getSourceByteCode();
        } else {
            String id = this.comment;
            int topOfStack = vm.peekStack();
            String splitID[] = id.split("<");
            String funcID = splitID[0];
            String formattedString = this.getSourceByteCode()
                    + "\texit " + funcID + ": " + topOfStack;
            return formattedString;
        }
    }
}
