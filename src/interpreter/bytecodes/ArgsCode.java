package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.Vector;

public class ArgsCode extends ByteCode {

    int numOfArgs;

    public ArgsCode() {
        super("ArgsCode");
    }

    @Override
    public void init(Vector<String> args) {
        if (!args.isEmpty()) {
            String firstArg = args.get(0);
            this.numOfArgs = Integer.parseInt(firstArg);
        }
    }

    @Override
    public void execute(VirtualMachine vm) {
        vm.newFrameAt(this.numOfArgs);
    }

    @Override
    public String getDebugLine(VirtualMachine vm) {
        return this.getSourceByteCode();
    }
}
