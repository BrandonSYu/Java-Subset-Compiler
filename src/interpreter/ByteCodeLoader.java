package interpreter;

import interpreter.bytecodes.ByteCode;
import java.io.*;
import java.util.Vector;

public class ByteCodeLoader {

    protected BufferedReader source;

    protected Program program;
    protected String line;
    protected int lineNum;

    public ByteCodeLoader(String programPath) throws IOException {
        source = new BufferedReader(new FileReader(programPath));
        program = new Program();
        line = "";
    }
    
    /*
    * Load codes from interpreter.bytecodes
    */
    public Program loadCodes() {
        try {
            while (source.ready()) {
                line = source.readLine();
                String sourceByteCode = line;
                if (line != null) {
                    String[] code = line.split("\\s");
                    Vector<String> args = new Vector();
                    if (code.length <= 0) {
                        ;
                    } else if (CodeTable.get(code[0]) != null) {
                        String codeTok = code[0];
                        String codeClass = CodeTable.get(codeTok);

                        ByteCode bytecode = (ByteCode) (Class.forName("interpreter.bytecodes." + codeClass).newInstance());

                        for (int i = 1; i < code.length; i++) {
                            args.add(code[i]);
                        }
                        bytecode.init(args);
                        bytecode.setSourceByteCode(sourceByteCode);
                        program.addCode(bytecode);
                    }
                }
            }
        } catch (Exception e) {//incase bytecodes are improperly loaded
            System.exit(1);
        }
        program.resolveAddress();
        return program;
    }
}
