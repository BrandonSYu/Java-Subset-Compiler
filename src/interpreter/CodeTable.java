package interpreter;

public class CodeTable {

    private static java.util.HashMap<String, String> codeMap = new java.util.HashMap<>();

    private static String[] bc = new String[]{ //string array containing all the necessary bytecodes
        "Args", "Bop", "Call", "Dump", "Falsebranch", "Goto", "Halt", "Label",
        "Lit", "Load", "Pop", "Read", "Return", "Store", "Write"
    };

    /*
    Dynamic generation of bytecodes
     */
    public static void init() {
        String bcClass;
        for (String bytecode : bc) {
            bcClass = bytecode + "Code";
            bytecode = bytecode.toUpperCase();
            codeMap.put(bytecode, bcClass);
        }
    }

    public static String get(String code) {
        return codeMap.get(code);
    }
}
