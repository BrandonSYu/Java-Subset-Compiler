package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.Vector;

public class ReadCode extends ByteCode {

    public ReadCode() {
        super("ReadCode");
    }

    @Override
    public void init(Vector<String> args) {
        ;
    }

    @Override
    public void execute(VirtualMachine vm) {
        int readVal = 0;
        readVal = vm.readInt();

        vm.pushRunStack(readVal);

    }

    @Override
    public String getDebugLine(VirtualMachine vm) {
        return this.getSourceByteCode();
    }
}
