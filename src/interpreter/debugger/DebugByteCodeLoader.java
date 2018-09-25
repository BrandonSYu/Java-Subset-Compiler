package interpreter.debugger;

import interpreter.ByteCodeLoader;
import interpreter.CodeTable;
import interpreter.Program;
import interpreter.bytecodes.ByteCode;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class DebugByteCodeLoader extends ByteCodeLoader {

    Set<Integer> breakpointSet;

    @Override
    public Program loadCodes() {
        try {
            do {
                line = source.readLine();
                String sourceByteCode = line;
                if (line != null) {
                    String[] splitString = line.split("\\s"); //seperate tokens
                    Vector<String> args = new Vector();
                    if (splitString.length <= 0) { //empty line
                        ;
                    } else {
                        ByteCode bytecode = null;
                        if (DebugCodeTable.get(splitString[0]) != null) {//check debug bytecodes
                            String codeTok = splitString[0];
                            if (codeTok.equals("LINE")) {//keep track of line bytecode for breakpoints 
                                breakpointSet.add(Integer.parseInt(splitString[1]));
                            }
                            String codeClass = DebugCodeTable.get(codeTok);
                            bytecode = (ByteCode) (Class.forName("interpreter.bytecodes.debuggerByteCodes." + codeClass).newInstance());
                        } else if (CodeTable.get(splitString[0]) != null) {//check bytecodes
                            String codeTok = splitString[0];
                            String codeClass = CodeTable.get(codeTok);
                            bytecode = (ByteCode) (Class.forName("interpreter.bytecodes." + codeClass).newInstance());
                        }
                        for (int i = 1; i < splitString.length; i++) {
                            args.add(splitString[i]);
                        }
                        bytecode.init(args);
                        bytecode.setSourceByteCode(sourceByteCode);//original source bytecode
                        program.addCode(bytecode);
                    }
                    lineNum++;
                }
            } while (line != null);

        } catch (Exception e) {
            System.exit(1);
        }
        program.resolveAddress();
        return program;
    }

    public DebugByteCodeLoader(String programFile) throws IOException {
        super(programFile);
        breakpointSet = new HashSet<>();
    }

    public Set<Integer> getValidBreakpointLines() {
        return breakpointSet;
    }
}
