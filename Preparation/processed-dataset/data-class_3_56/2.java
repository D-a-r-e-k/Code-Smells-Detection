/**
     * Build a control flow graph for a method's instruction list,
     * apply any transformers to it, and collapse the graph into
     * a new instruction list.
     *
     * @param clazz  Class being transformed.
     * @param method The method being transformed.
     * @return   The transformed instruction list.
     */
public InstructionList xform(ClassGen clazz, MethodGen method) {
    if (method == null) {
        throw new IllegalArgumentException("null method");
    }
    ControlFlowGraph graph = makeGraph(clazz, method);
    GraphXformer[] xf = new GraphXformer[gxf.size()];
    // apply each graph processor in turn 
    for (int i = 0; i < gxf.size(); i++) {
        try {
            xf[i] = (GraphXformer) ((gxf.get(i)).getClass().newInstance());
        } catch (IllegalAccessException e) {
            zapGraphXformer(xf[i], e);
        } catch (InstantiationException e) {
            zapGraphXformer(xf[i], e);
        }
        if (xf[i] != null && graph != null) {
            xf[i].xform(clazz, method, graph);
        }
    }
    if (graph == null) {
        return null;
    }
    BytecodeCollector bc = collapseGraph(graph);
    ilist = bc.getInstructionList();
    if (ilist == null) {
        return null;
    }
    if (ts == null) {
        ceg = null;
    } else {
        // this is guaranteed not to be null, but it may be empty 
        ceg = bc.getCEGs(ts.getCatchData());
        if (ceg.length != handlers.length) {
            System.out.println("GraphTransformer.xform WARNING - PROBABLE INTERNAL ERROR:\n   method had " + handlers.length + " exception handlers, but after graph transformation " + ceg.length);
        }
    }
    return ilist;
}
