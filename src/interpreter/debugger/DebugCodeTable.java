package interpreter.debugger;

import interpreter.CodeTable;

public class DebugCodeTable {

    private static java.util.HashMap<String, String> codeMap = new java.util.HashMap<>();

    private static String[] bc = new String[]{ //string array containing all the necessary debug bytecodes
        "Call", "Formal", "Function", "Line",
        "Lit", "Pop", "Return"
    };

    public static String get(String code) {
        return codeMap.get(code);
    }

    public static void init() {
        String bcClass;
        for (String bytecode : bc) {
            bcClass = "Debug" + bytecode + "Code";
            bytecode = bytecode.toUpperCase();
            codeMap.put(bytecode, bcClass);
        }
    }
}
