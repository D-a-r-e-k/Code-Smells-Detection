/**
     * Collapse a method control flow graph, writing bytecode back
     * into the MethodGen data structures.
     */
protected final BytecodeCollector collapseGraph(ControlFlowGraph graph) {
    BytecodeCollector theMan = new BytecodeCollector();
    new Walker().visit(graph, theMan);
    return theMan;
}
