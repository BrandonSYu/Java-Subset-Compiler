package interpreter;

import interpreter.bytecodes.ByteCode;
import interpreter.bytecodes.CallCode;
import interpreter.bytecodes.FalsebranchCode;
import interpreter.bytecodes.GotoCode;
import interpreter.bytecodes.LabelCode;
import interpreter.bytecodes.debuggerByteCodes.DebugCallCode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Program {

    private int codeNum;
    private List<ByteCode> byteCodeList; //stores each bytecode
    private Map<String, List<ByteCode>> labelToObjectMap; //hash map for the label to bytecode objects
    private Map<String, Integer> labelToAddressMap; //hashmap for label to symbolic address

    public Program() {
        codeNum = 0; //current symbolic address
        byteCodeList = new ArrayList<>();
        labelToObjectMap = new HashMap<>();
        labelToAddressMap = new HashMap<>();
    }

    /*
    * Add Bytecode method
    */
    public void addCode(ByteCode bytecode) {
        String codeName = bytecode.getByteCodeName();

        if (codeName.matches("GotoCode")|| codeName.matches("FalsebranchCode")
                || codeName.matches("CallCode")|| codeName.matches("DebugCallCode")) 
        { //check for Goto, Falsebranch, CallCode and DebugCallCode
            String label = null;
            if (codeName.equals("GotoCode")) {
                GotoCode gotoByteCode = (GotoCode) bytecode;
                label = gotoByteCode.getAddress();
            } else if (codeName.equalsIgnoreCase("FalsebranchCode")) {
                FalsebranchCode falseBranchCode = (FalsebranchCode) bytecode;
                label = falseBranchCode.getAddress();
            } else if (codeName.equals("CallCode")) {
                CallCode callCode = (CallCode) bytecode;
                label = callCode.getAddress();
            } else if (codeName.equals("DebugCallCode")) {
                CallCode callCode = (DebugCallCode) bytecode;
                label = callCode.getAddress();
            }
            if (labelToObjectMap.containsKey(label)) {
                List byteCodeLabelList = labelToObjectMap.get(label);

                byteCodeLabelList.add(bytecode);
            } else {

                List<ByteCode> byteCodeLabelList = new ArrayList<>();
                byteCodeLabelList.add(bytecode);

                labelToObjectMap.put(label, byteCodeLabelList);
            }
        } else if (codeName.equals("LabelCode")) {
            String label = null;
            LabelCode labelCode = (LabelCode) bytecode;
            label = labelCode.getLabel();
            if (labelToAddressMap.containsKey(label)) {
                System.out.println("Invalid bytecode!");
                System.exit(1);
            } else {
                labelToAddressMap.put(label, codeNum);
            }

        }

        byteCodeList.add(bytecode); //add bytecode to list 
        codeNum++; //increment symbolic address

    }

    public void printProgram() {
        System.out.println("\nDump: \n");
        for (ByteCode bc : byteCodeList) {

            System.out.println(bc + "\n");
        }
    }

    public ByteCode getCode(int bcNum) {
        return byteCodeList.get(bcNum);
    }

    public void resolveAddress() {
        for (String label : labelToAddressMap.keySet()) {
            int symbolicAddress = labelToAddressMap.get(label); //get symbolic addresses

            List branchByteCodes = labelToObjectMap.get(label); //get list of bytecodes for label

            if (branchByteCodes != null) {
                for (Iterator it = branchByteCodes.iterator(); it.hasNext();) {
                    ByteCode bc = (ByteCode) it.next();

                    String codeName = bc.getByteCodeName();

                    if (codeName.equals("GotoCode")) {
                        GotoCode gotoCode = (GotoCode) bc;
                        gotoCode.setAddress(Integer.toString(symbolicAddress));
                    } else if (codeName.equals("FalsebranchCode")) {
                        FalsebranchCode falseBranchCode = (FalsebranchCode) bc;
                        falseBranchCode.setAddress(Integer.toString(symbolicAddress));
                    } else if (codeName.equals("CallCode")) {
                        CallCode callCode = (CallCode) bc;
                        callCode.setAddress(Integer.toString(symbolicAddress));
                    } else if (codeName.equals("DebugCallCode")) {
                        CallCode callCode = (DebugCallCode) bc;
                        callCode.setAddress(Integer.toString(symbolicAddress));
                    }
                }
            }
        }
    }
}
