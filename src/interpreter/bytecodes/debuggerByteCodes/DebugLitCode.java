package interpreter.bytecodes.debuggerByteCodes;

import interpreter.VirtualMachine;
import interpreter.bytecodes.LitCode;
import interpreter.debugger.DebugVM;
import java.util.Vector;

public class DebugLitCode extends LitCode {

    private String symbol;
    private int offset;
    private boolean setSymbol = false;

    public DebugLitCode() {
        this.byteCodeName = "DebugLitCode";
    }

    @Override
    public void execute(VirtualMachine vm) {
        super.execute(vm);
        try {
            DebugVM debugVM = (DebugVM) vm;

            if (setSymbol) {

                debugVM.enterSymbolAtCurrentOffset(symbol);
            }
        } catch (Exception e) {
            System.exit(1);
        }
    }

    @Override
    public void init(Vector<String> args) {
        super.init(args);
        if (!args.isEmpty()) {
            if (args.size() == 2) {
                setSymbol = true;
                symbol = args.get(1);
                offset = Integer.valueOf(args.get(0));
            }
        }
    }
}
