package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.Vector;

public class LitCode extends ByteCode {

    protected int value;
    protected String comment;
    protected int numArgs;

    public LitCode() {
        super("LitCode");
    }

    @Override
    public void init(Vector<String> args) {
        if (args.size() == 1) {
            this.numArgs = 1;
            this.value = Integer.parseInt(args.get(0));
            this.comment = null;
        } else {
            this.numArgs = 2;
            this.value = Integer.parseInt(args.get(0));
            this.comment = args.get(1);
        }
    }

    @Override
    public void execute(VirtualMachine vm) {
        vm.pushRunStack(this.value);

    }

    @Override
    public String getDebugLine(VirtualMachine vm) {
        if (this.numArgs == 1) {
            return this.getSourceByteCode();
        } else {
            String formattedString = this.getSourceByteCode()
                    + "\t\tint " + this.comment;
            return formattedString;
        }
    }
}
