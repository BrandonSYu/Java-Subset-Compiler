package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.Vector;

public class WriteCode extends ByteCode {

    public WriteCode() {
        super("WriteCode");
    }

    @Override
    public void init(Vector<String> args) {
        ;
    }

    @Override
    public void execute(VirtualMachine vm) {
        vm.writeInt();
    }

    @Override
    public String getDebugLine(VirtualMachine vm) {
        return this.getSourceByteCode();
    }
}
