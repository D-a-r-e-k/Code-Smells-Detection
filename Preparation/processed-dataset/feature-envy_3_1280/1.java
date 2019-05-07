/**
         * Instrument edges inbound to code vertex.
         */
public void discoverEdge(Edge e) {
    Vertex source = e.getSource();
    Vertex target = e.getTarget();
    ControlFlowGraph srcGraph = (ControlFlowGraph) source.getGraph();
    ControlFlowGraph tgtGraph = (ControlFlowGraph) target.getGraph();
    // DEBUG 
    if (tgtGraph != graph) {
        System.out.println("GraphAction.discoverEdge:\n" + "  current graph is " + graph.getIndex() + " but edge is " + e);
    }
    // END 
    boolean addingCounter = true;
    if ((target instanceof CodeVertex) && !(target instanceof CounterVertex) && !(source instanceof CounterVertex)) {
        Instruction srcConnInst = null;
        if (source instanceof CodeVertex) {
            srcConnInst = ((CodeVertex) source).getConnInst();
            if (srcConnInst != null && srcConnInst instanceof GotoInstruction && (source.getGraph() != tgtGraph)) {
                // Don't add a counter if the goto jumps out of  
                // this graph.  XXX needs more thought! -- this 
                // eliminates some test failures but will fail to 
                // count some statement hits 
                addingCounter = false;
            }
        }
        // XXX A BIT OF A KLUDGE (NestedTryBlocks bugfix) 
        if (srcGraph != graph) {
            System.out.println("GraphAction.discoverEdge WARNING: " + "graph index " + graph.getIndex() + "\n    but edge is " + e + " - not adding counter");
            addingCounter = false;
        }
        if (addingCounter) {
            CounterVertex cv;
            if (source instanceof Exit) {
                // MOVED UP 
                //                      ControlFlowGraph tgtGraph 
                //                                  = (ControlFlowGraph)target.getGraph(); 
                cv = (CounterVertex) tgtGraph.insertCodeVertex((CodeVertex) new CounterVertex(tgtGraph), e);
            } else {
                cv = (CounterVertex) graph.insertCodeVertex((CodeVertex) new CounterVertex(graph), e);
            }
            InstructionList ilist = cv.getInstructionList();
            InstructionHandle ih = ilist.append(factory_.createFieldAccess(clazz_.getClassName(), "q$$q", new ArrayType(Type.INT, 1), Constants.GETSTATIC));
            ilist.append(new PUSH(cpGen_, counterCount++));
            ilist.append(InstructionConstants.DUP2);
            ilist.append(InstructionConstants.IALOAD);
            ilist.append(new PUSH(cpGen_, 1));
            ilist.append(InstructionConstants.IADD);
            ilist.append(InstructionConstants.IASTORE);
            // end instruction list /////////////////// 
            // RETARGETING ////////////////////////////////////// 
            if (source instanceof CodeVertex) {
                if (srcConnInst != null) {
                    // UNARY //////////////////////////////////// 
                    // BINARY /////////////////////////////////// 
                    if (srcConnInst instanceof GotoInstruction) {
                        if (!(e == source.getEdge())) {
                            ((CodeVertex) source).moveGoto(cv);
                        }
                    } else if (srcConnInst instanceof IfInstruction || srcConnInst instanceof JsrInstruction) {
                        Edge otherEdge = ((BinaryConnector) source.getConnector()).getOtherEdge();
                        if (e == otherEdge) {
                            // this is the 'else' branch; the  
                            // counter needs to have a Goto 
                            // connecting instruction 
                            BranchInstruction bi = (BranchInstruction) srcConnInst;
                            InstructionHandle targ = bi.getTarget();
                            bi.setTarget(ih);
                            cv.setConnInst(new GOTO(targ));
                            // ignore the edge returned 
                            cv.makeBinary();
                            BinaryConnector bc = (BinaryConnector) cv.getConnector();
                            Vertex myTarget = bc.getTarget();
                            // main edge is the flow-through exit 
                            // DEBUG 
                            if (graph.getExit() == null) {
                                System.out.println("GRAPH HAS NULL EXIT");
                            }
                            // END 
                            bc.setTarget(graph.getExit());
                            bc.setOtherTarget(myTarget);
                        }
                    }
                }
            }
        }
    }
}
