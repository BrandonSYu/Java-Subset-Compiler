package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.Vector;

public class LabelCode extends ByteCode {

    private String label;

    public LabelCode() {
        super("LabelCode");
    }

    @Override
    public void init(Vector<String> args) {

        if (!args.isEmpty()) {
            this.label = args.get(0);
        }
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String inLabel) {
        this.label = inLabel;
    }

    @Override
    public void execute(VirtualMachine vm) {
    }

    @Override
    public String getDebugLine(VirtualMachine vm) {
        return this.getSourceByteCode();
    }
}
