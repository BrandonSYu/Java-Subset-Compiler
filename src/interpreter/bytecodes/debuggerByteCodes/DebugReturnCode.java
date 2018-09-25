package interpreter.bytecodes.debuggerByteCodes;

import interpreter.VirtualMachine;
import interpreter.bytecodes.ReturnCode;
import interpreter.debugger.DebugVM;

public class DebugReturnCode extends ReturnCode {

    public DebugReturnCode() {
        this.byteCodeName = "DebugReturnCode";
    }

    @Override
    public void execute(VirtualMachine vm) {
        super.execute(vm);
        try {
            DebugVM debugVM = (DebugVM) vm;

            debugVM.popEnvironmentRecord();

        } catch (NumberFormatException e) {
            System.exit(1);
        }
    }
}
