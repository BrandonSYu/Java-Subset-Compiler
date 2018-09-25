package interpreter.bytecodes.debuggerByteCodes;

import interpreter.VirtualMachine;
import interpreter.bytecodes.CallCode;
import interpreter.debugger.DebugVM;
import java.util.Vector;

public class DebugCallCode extends CallCode {

    public DebugCallCode() {
        this.byteCodeName = "DebugCallCode";
    }

    @Override
    public void execute(VirtualMachine vm) {
        super.execute(vm);
        try {
            DebugVM debugVM = (DebugVM) vm;
            debugVM.createNewEnvironmentRecord();
            debugVM.setJustEnteredFunction();

        } catch (Exception e) {
            System.exit(1);
        }
    }
}
