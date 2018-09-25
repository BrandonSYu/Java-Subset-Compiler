package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.Vector;

public class StoreCode extends ByteCode {

    private int offset;
    private String comment;
    private int numArgs;

    public StoreCode() {
        super("StoreCode");
    }

    @Override
    public void init(Vector<String> args) {
        if (!args.isEmpty()) {
            this.offset = Integer.parseInt(args.get(0));
            if (args.size() == 2) {
                this.comment = args.get(1);
            }
            this.numArgs = args.size();
        }
    }

    @Override
    public void execute(VirtualMachine vm) {
        vm.storeRunStack(this.offset);
    }

    @Override
    public String getDebugLine(VirtualMachine vm) {

        if (this.numArgs == 1) {
            return this.getSourceByteCode();
        } else {
            int topOfStack = vm.peekStack();
            String formattedString = this.getSourceByteCode()
                    + "\t" + this.comment + " = " + topOfStack;
            return formattedString;
        }
    }
}
