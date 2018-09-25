package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.Vector;

public abstract class ByteCode {

    protected String byteCodeName;
    protected String sourceByteCode;

    public ByteCode(String bcName) {
        this.byteCodeName = bcName;
    }

    public abstract void init(Vector<String> args);

    public void setSourceByteCode(String in) {
        sourceByteCode = in;
    }

    public String getSourceByteCode() {
        return sourceByteCode;
    }

    public String getByteCodeName() {
        return this.byteCodeName;
    }

    public abstract void execute(VirtualMachine vm);

    public abstract String getDebugLine(VirtualMachine vm);

}
