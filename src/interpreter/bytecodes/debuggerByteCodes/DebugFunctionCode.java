package interpreter.bytecodes.debuggerByteCodes;

import interpreter.VirtualMachine;
import interpreter.bytecodes.ByteCode;
import interpreter.debugger.DebugVM;
import java.util.Vector;

public class DebugFunctionCode extends ByteCode {

    private String funcName;
    private int startLine;
    private int stopLine;

    public DebugFunctionCode() {
        super("DebugFormalCode");
    }

    @Override
    public void execute(VirtualMachine vm) {

        try {
            DebugVM debugVM = (DebugVM) vm;

            debugVM.setFunction(funcName, startLine, stopLine);

            debugVM.unsetFunc();

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
            funcName = args.get(0);
            startLine = Integer.valueOf(args.get(1));
            stopLine = Integer.valueOf(args.get(2));
        }
    }
}
