package interpreter.debugger;

public class SourceLineEntry {

    boolean isBreakptSet;
    String sourceLine;

    public SourceLineEntry(String sourceLine, boolean isBreakPointSet) {
        this.isBreakptSet = isBreakPointSet;
        this.sourceLine = sourceLine;
    }

    public boolean isBreakPointSet() { //check if breakpoint is set
        return this.isBreakptSet;
    }

    public void setBreakPoint() { //set breakpoint
        isBreakptSet = true;
    }

    public void unsetBreakPoint() {//unset breakpoint
        isBreakptSet = false;
    }

    public String getSourceLine() { //get source
        return this.sourceLine;
    }

    public void setSourceLine(String source) {
        sourceLine = source;
    }
}
