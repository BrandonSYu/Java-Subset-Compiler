package interpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

public class RunTimeStack {

    private Stack<Integer> framePoint;
    private Vector runStack;

    private int runStackPtr;

    public RunTimeStack() {
        framePoint = new Stack<Integer>();
        runStack = new Vector<Integer>();
        runStackPtr = 0;
    }

    /*
    * Dump runtime stack info for debugging
     */
    public void dump() {
        System.out.print("[");
        for (int i = 0; i < runStack.size(); i++) {
            if (i != 0 && framePoint.contains(i)) {
                System.out.print("] [");
            }

            if (!framePoint.contains(i)) {
                System.out.print(",");
            }

            System.out.print(runStack.elementAt(i));
        }
        System.out.println("]");
    }

    public List getArgs() {
        List returnList = new ArrayList();

        int startIndex = 0;
        int stopIndex = runStackPtr;

        if (!framePoint.isEmpty()) {
            //Peek at the top frame
            int nextFrame = framePoint.peek();
            startIndex = nextFrame + 1;
        }

        for (int i = startIndex; i < stopIndex; i++) {
            returnList.add(runStack.get(i));
        }

        return returnList;

    }

    /*
    * Peeks at the top element of the stack
     */
    public int peek() {
        return (int) runStack.get(runStackPtr - 1);
    }

    /*
    * Returns the element at an index of the runtime stack
     */
    public int elementAt(int offset) {
        int frame = 0;

        if (framePoint.isEmpty()) {
            frame = 0;
        } else {
            int frObj = framePoint.peek();

            frame = frObj + 1;
        }
        int value = (int) runStack.get(frame + offset);
        return value;
    }

    /*
    *Pop first item off of runtime stack
     */
    public int pop() {
        int popVal = (int) runStack.get(runStackPtr - 1);
        runStack.set(runStackPtr - 1, 0);
        runStackPtr--;
        return popVal;
    }

    /*
    * Push item onto stack
     */
    public int push(int i) {

        runStack.add(runStackPtr, i);
        runStackPtr++;
        return i;
    }

    /*
    * Starts new frame at offset
     */
    public void newFrameAt(int offset) {
        int rStackPtr = (runStackPtr - 1) - offset;
        framePoint.add(rStackPtr);

    }

    /*
    * Pop the top of the stack after after returning from a frame
     */
    public void popFrame() {
        int popVal = this.pop();
        int frObj = framePoint.pop();
        int rStackPtr = frObj;
        this.runStackPtr = (rStackPtr + 1);
        this.push(popVal);
    }

    /*
    * Store into variables
     */
    public int store(int offset) {
        int frame = 0;

        if (!framePoint.isEmpty()) {
            int frObj = framePoint.peek();
            frame = frObj;
        } else {
            frame = 0;
        }

        int topVal = this.pop();
        runStack.set(frame + offset, topVal);

        return topVal;
    }

    public int getCurrentStackPtr() {
        return this.runStackPtr;
    }

    public int load(int offset) {
        int frame = 0;

        if (framePoint.isEmpty()) {
            frame = 0;
        } else {
            int frObj = framePoint.peek();

            frame = frObj + 1;
        }
        int value = (int) runStack.get(frame + offset);
        this.push(value);
        return value;
    }

    public int size() {
        return runStack.size();
    }

    public int frames() {
        return framePoint.size();
    }

    /*
    * Store literals onto the stack
     */
    public Integer push(Integer i) {
        runStack.add(i);
        runStackPtr++;
        return i;
    }

    public boolean isEmpty() {
        return runStack.isEmpty();
    }
}
