package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.Vector;

public class PopCode extends ByteCode {

    protected int numToPop;

    public PopCode() {
        super("PopCode");
    }

    @Override
    public void init(Vector<String> args) {
        if (!args.isEmpty()) {
            this.numToPop = Integer.parseInt(args.get(0));
        }
    }

    @Override
    public void execute(VirtualMachine vm) {
        for (int i = 0; i < this.numToPop; i++) {
            vm.popRunStack();
        }
    }

    @Override
    public String getDebugLine(VirtualMachine vm) {
        return this.getSourceByteCode();
    }
}
