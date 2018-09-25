package interpreter.debugger;

import java.util.Iterator;
import java.util.Set;

class Binder {//symbol table mechanism using binder marks

    /**
     * <
     * pre>
     * Binder objects group 3 fields 1. a value 2. the next link in the chain of
     * symbols in the current scope 3. the next link of a previous Binder for
     * the same identifier in a previous scope
     * </pre>
     */
    private Object value;
    private String prevtop;
    private Binder tail;

    Binder(Object v, String p, Binder t) {
        value = v;
        prevtop = p;
        tail = t;
    }

    Object getValue() {
        return value;
    }

    String getPrevtop() {
        return prevtop;
    }

    Binder getTail() {
        return tail;
    }
}

public class DebugSymTable {

    private java.util.HashMap<String, Binder> symbols = new java.util.HashMap<String, Binder>();
    private java.util.Stack<String> prevKeys = new java.util.Stack<String>();
    private String top;
    private Binder marks;

    public DebugSymTable() {
    }

    @Override
    public String toString() {
        String returnVal = "";
        Set<String> keys = symbols.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String element = iterator.next();
            Binder bind = symbols.get(element);
            returnVal += element + "/" + bind.getValue();
            if (iterator.hasNext()) {
                returnVal += ",";
            }
        }
        return returnVal;
    }

    public Object get(String key) {
        Binder e = symbols.get(key);
        return e.getValue();
    }

    public void put(String key, Object value) {
        symbols.put(key, new Binder(value, top, symbols.get(key)));
        prevKeys.push(key);
        top = key;
    }

    public void beginScope() {
        marks = new Binder(null, top, marks);
        top = null;
    }

    public void endScope() {
        while (top != null) {
            Binder e = symbols.get(top);
            if (e.getTail() != null) {
                symbols.put(top, e.getTail());
            } else {
                symbols.remove(top);
            }
            top = e.getPrevtop();
        }
        top = marks.getPrevtop();
        marks = marks.getTail();
    }

    public void pop(int n) {
        for (int i = 0; i < n; i++) {
            String key = prevKeys.pop();
            Binder bind = symbols.get(key);
            if (bind.getTail() != null) {
                Binder oldBind = bind.getTail();
                symbols.remove(key);
                symbols.put(key, oldBind);
            } else {
                symbols.remove(key);
            }
        }
    }

    public java.util.Set<String> keys() {
        return symbols.keySet();
    }
}
