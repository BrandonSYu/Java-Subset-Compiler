package interpreter.bytecodes.debuggerByteCodes;

import interpreter.VirtualMachine;
import interpreter.bytecodes.PopCode;
import interpreter.debugger.DebugVM;
import java.util.Vector;

public class DebugPopCode extends PopCode {

    public DebugPopCode() {
        this.byteCodeName = "DebugPopCode";
    }

    @Override
    public void execute(VirtualMachine vm) {
        super.execute(vm);
        try {
            DebugVM debugVM = (DebugVM) vm;

            debugVM.popSymbolTable(numToPop);

        } catch (Exception e) {
            System.exit(1);
        }
    }
}
