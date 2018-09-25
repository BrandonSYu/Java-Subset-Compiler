package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.Vector;

public class GotoCode extends ByteCode {

    private String jumpAddr;

    public GotoCode() {
        super("GotoCode");
    }

    @Override
    public void init(Vector<String> args) {
        if (!args.isEmpty()) {
            this.jumpAddr = args.get(0);
        }
    }

    @Override
    public void execute(VirtualMachine vm) {
        vm.setProgramCounter(Integer.parseInt(this.jumpAddr));
    }

    @Override
    public String getDebugLine(VirtualMachine vm) {
        return this.getSourceByteCode();
    }

    public String getAddress() {
        return this.jumpAddr;
    }

    public void setAddress(String addr) {
        this.jumpAddr = addr;
    }
}
