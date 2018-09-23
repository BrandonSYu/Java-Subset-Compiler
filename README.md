# Java-Subset-Compiler

A compiler for a subset of java for my CSC 413 Software Development Course.

Project built entirely out of java emulating the compilation of source code on the Java Virtual Machine using OOP through lexical analysis of the source code as a stream of characters into tokens which are then parsed using recursive descent processing. These tokens are put onto an Abstract Syntax Tree, which then is constrained by type checks, and reference to a symbol table. Bytecodes are generated from this decorated abstract syntax tree, for which it is then put onto a runtime stack to form a bytecode program. The bytecode program is then interpreted by our abstract virtual machine. A makeshift debugger is built to properly step in, step out, and set breakpoints within our bytecode program.

To run: java interpreter factorial.x.cod
