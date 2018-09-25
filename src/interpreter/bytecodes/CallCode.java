package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.List;
import java.util.Vector;

public class CallCode extends ByteCode {

    private String jumpAddr;

    public CallCode() {
        super("CallCode");
    }

    @Override
    public void init(Vector<String> args) {
        if (!args.isEmpty()) {
            this.jumpAddr = args.get(0);
        }
    }

    @Override
    public void execute(VirtualMachine vm) {
        vm.pushReturnAddrs();
        vm.setProgramCounter(Integer.parseInt(this.jumpAddr));
    }

    public String getAddress() {
        return this.jumpAddr;
    }

    public void setAddress(String addr) {
        this.jumpAddr = addr;
    }

    @Override
    public String getDebugLine(VirtualMachine vm) {
        String id = this.getSourceByteCode();
        List argList = vm.getArgs();
        String splitID[] = id.split("\\s");
        String id2 = splitID[1];

        String splitID2[] = id2.split("<<");
        String funcID = splitID2[0];

        String formattedString = this.getSourceByteCode()
                + "\t" + funcID + "(";

        for (int i = 0; i < argList.size(); i++) {
            formattedString += argList.get(i);
            if (i != (argList.size() - 1)) {
                formattedString += ",";
            }
        }
        formattedString += ")";
        return formattedString;
    }
}
