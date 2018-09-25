package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.Vector;

public class DumpCode extends ByteCode {

    String toggle;

    public DumpCode() {
        super("DumpCode");
    }

    @Override
    public void execute(VirtualMachine vm) {
        if (this.toggle.equals("ON")) {
            vm.dumpOn();
        } else {
            vm.dumpOff();
        }
    }

    @Override
    public String getDebugLine(VirtualMachine vm) {
        return null;
    }

    @Override
    public void init(Vector<String> args) {
        if (!args.isEmpty()) {
            this.toggle = args.get(0);
        }
    }
}
