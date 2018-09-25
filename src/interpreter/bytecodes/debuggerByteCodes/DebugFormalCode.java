package interpreter.bytecodes.debuggerByteCodes;

import interpreter.VirtualMachine;
import interpreter.bytecodes.ByteCode;
import interpreter.debugger.DebugVM;
import java.util.Vector;

public class DebugFormalCode extends ByteCode {

    private String symbol;
    private String value;

    public DebugFormalCode() {
        super("DebugFormalCode");
    }

    @Override
    public void execute(VirtualMachine vm) {
        try {
            DebugVM debugVM = (DebugVM) vm;
            int offset = Integer.valueOf(this.value);
            debugVM.enterSymbolAtOffset(symbol, offset);

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
            this.symbol = args.get(0);
            this.value = args.get(1);
        }
    }

}
