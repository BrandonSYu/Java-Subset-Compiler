package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.Vector;

public class HaltCode extends ByteCode {

    public HaltCode() {
        super("HaltCode");
    }

    @Override
    public void init(Vector<String> args) {
    }

    @Override
    public void execute(VirtualMachine vm) {
        vm.stopRunning();
    }

    @Override
    public String getDebugLine(VirtualMachine vm) {
        return this.getSourceByteCode();
    }
}
