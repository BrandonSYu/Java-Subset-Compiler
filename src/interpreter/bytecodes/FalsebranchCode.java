package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.Vector;

public class FalsebranchCode extends ByteCode {

    private String jumpAddr;

    public FalsebranchCode() {
        super("FalsebranchCode");
    }

    @Override
    public void init(Vector<String> args) {
        if (!args.isEmpty()) {
            this.jumpAddr = args.get(0);
        }
    }

    @Override
    public void execute(VirtualMachine vm) {
        int branch = vm.popRunStack();
        if (branch == 0) {

            vm.setProgramCounter(Integer.parseInt(this.jumpAddr));
        }
    }

    public String getAddress() {
        return this.jumpAddr;
    }

    public void setAddress(String addr) {
        this.jumpAddr = addr;
    }

    @Override
    public String getDebugLine(VirtualMachine vm) {
        return this.getSourceByteCode();
    }

}
