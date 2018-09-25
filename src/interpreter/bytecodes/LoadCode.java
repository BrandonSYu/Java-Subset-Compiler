package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.Vector;

public class LoadCode extends ByteCode {

    private int offset;
    private String comment;
    private int numArgs;

    public LoadCode() {
        super("LoadCode");
    }

    @Override
    public void init(Vector<String> args) {
        if (!args.isEmpty()) {
            this.numArgs = args.size();
            this.offset = Integer.parseInt(args.get(0));
            if (args.size() == 2) {
                this.comment = args.get(1);
            }
        }
    }

    @Override
    public void execute(VirtualMachine vm) {
        vm.loadRunStack(this.offset);

    }

    @Override
    public String getDebugLine(VirtualMachine vm) {

        if (this.numArgs == 1) {
            return this.getSourceByteCode();
        } else {
            String formattedString = this.getSourceByteCode()
                    + "\t<load " + this.comment + ">";
            return formattedString;
        }
    }
}
