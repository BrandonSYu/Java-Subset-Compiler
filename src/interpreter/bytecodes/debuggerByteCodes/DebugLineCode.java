package interpreter.bytecodes.debuggerByteCodes;

import interpreter.VirtualMachine;
import interpreter.bytecodes.ByteCode;
import interpreter.debugger.DebugVM;
import java.util.Vector;

public class DebugLineCode extends ByteCode {

    public DebugLineCode() {
        super("DebugLineCode");
    }

    private int currLine;

    @Override
    public void execute(VirtualMachine vm) {
        try {
            DebugVM debugVM = (DebugVM) vm;

            debugVM.setCurrentLine(currLine);

        } catch (Exception e) {
            System.exit(1);
        }
    }

    @Override
    public String getDebugLine(VirtualMachine vm) {
        return this.getSourceByteCode();
    }

    @Override
    public void init(Vector<String> args) {
        if (!args.isEmpty()) {
            currLine = Integer.valueOf(args.get(0));
        }
    }
}
