/**
     * Build the graph for the method.
     * 
     * @param clazz Class being transformed in bcel ClassGen. 
     * @param m  BCEL representation of method we are working on.
     */
protected final ControlFlowGraph makeGraph(ClassGen clazz, MethodGen method) {
    SortedBlocks blox = new SortedBlocks();
    handlers = method.getExceptionHandlers();
    ControlFlowGraph cfg = new ControlFlowGraph();
    ControlFlowGraph currGraph = cfg;
    Edge e = cfg.getEntry().getEdge();
    ts = null;
    boolean startBlock = false;
    CodeVertex currV = null;
    LineNumberTable lineTab = method.getLineNumberTable(clazz.getConstantPool());
    if (handlers.length > 0) {
        // NEED TO ADJUST EDGE HERE  
        ts = new TryStacks(handlers, blox, cfg);
    }
    if (blox.exists(0)) {
        // we must have a try block starting at 0 
        currV = blox.get(0);
    } else {
        currV = blox.find(0, currGraph, e);
    }
    if (lineTab != null) {
        currV.setStartLine(lineTab.getSourceLine(0));
    }
    e = currV.getEdge();
    currGraph = (ControlFlowGraph) currV.getGraph();
    // Walk through the method's bytecode, appending it to the  
    // current vertex, creating new vertices where necessary. 
    InstructionList iList = method.getInstructionList();
    InstructionHandle currHandle = iList.getStart();
    Instruction inst = currHandle.getInstruction();
    int pos = currHandle.getPosition();
    // current vertex's InstructionList 
    InstructionList vIList = currV.getInstructionList();
    while (currHandle != null) {
        if (startBlock) {
            startBlock = false;
            if (e == null) {
                if (!blox.exists(pos)) {
                    // XXX this was formerly regarded as an 
                    // error; handling it like this makes it 
                    // clearer what the problem is, but we now get 
                    //     Falling off the end of the code 
                    currV = new CodeVertex(currGraph, pos);
                } else {
                    currV = blox.get(pos);
                }
            } else {
                currV = blox.find(pos, currGraph, e);
            }
            if (lineTab != null) {
                currV.setStartLine(lineTab.getSourceLine(pos));
            }
            e = currV.getEdge();
            currGraph = (ControlFlowGraph) currV.getGraph();
            // DEBUG 
            //System.out.println("makeGraph while; e = " + e); 
            // END 
            vIList = currV.getInstructionList();
        }
        if (inst instanceof GotoInstruction) {
            // to make the layout code (BytecodeCollector) work, 
            // introduce the notion of a 'virtual' edge; there is 
            // no flow of control, but the code along the virtual 
            // edge will be laid out first 
            Edge otherEdge = currV.makeBinary();
            currV.setConnInst(inst);
            int tpos = ((GotoInstruction) inst).getTarget().getPosition();
            int endTry;
            if (ts == null) {
                endTry = -1;
            } else {
                endTry = ts.getEndTry(currGraph);
            }
            if (endTry >= 0 && tpos > endTry) {
                // tpos is beyond end of try block and should be the 
                // first code vertex following the subgraph Exit; in  
                // any case the edge target becomes the Exit 
                Exit currExit = currGraph.getExit();
                otherEdge.setTarget(currExit);
                if (!blox.exists(tpos)) {
                    Vertex vFinal;
                    for (vFinal = currExit; vFinal.getTarget() instanceof Entry; vFinal = vFinal.getTarget()) {
                        ;
                    }
                    blox.add(tpos, vFinal.getEdge());
                }
            } else {
                // tpos is within try block; make v target of e 
                blox.find(tpos, currGraph, otherEdge);
            }
            // continue to use this 'virtual' edge 
            startBlock = true;
        } else if (inst instanceof IfInstruction || inst instanceof JSR) {
            Edge otherEdge = currV.makeBinary();
            currV.setConnInst(inst);
            // handle 'then' branch or target of JSR 
            int tpos = ((BranchInstruction) inst).getTarget().getPosition();
            //  edge for 'then' vertex 
            blox.find(tpos, currGraph, otherEdge);
            // continue to use the current edge 
            startBlock = true;
        } else if (inst instanceof ReturnInstruction || inst instanceof RET) {
            currV.setConnInst(inst);
            e = null;
            startBlock = true;
        } else if (inst instanceof InvokeInstruction) {
            currV.setConnInst(inst);
            // continue to use the current edge 
            startBlock = true;
        } else if (inst instanceof Select) {
            InstructionHandle[] targets = ((Select) inst).getTargets();
            //MultiConnector conn = currV.makeMulti(targets.length); 
            ComplexConnector conn = currV.makeComplex(targets.length);
            currV.setConnInst(inst);
            for (int i = 0; i < targets.length; i++) {
                int tpos = targets[i].getPosition();
                blox.find(tpos, currGraph, conn.getEdge(i));
            }
            // EXPERIMENT IN HANDLING THE DEFAULT - seems to work ... 
            InstructionHandle theDefault = ((Select) inst).getTarget();
            if (theDefault != null) {
                blox.find(theDefault.getPosition(), currGraph, conn.getEdge());
            }
            e = null;
            // it's an n-way goto 
            startBlock = true;
        } else if (inst instanceof ExceptionThrower) {
            // Instructions which might or do (ATHROW) cause 
            // an exception.  XXX This needs to be looked at 
            // more carefully!  There are 22 such instructions 
            // or groups; these include NEW, LDIV, and 
            // ReturnInstruction.  Splitting blocks here causes 
            // a very large increase in the number of vertices; 
            // the benefit is unclear. 
            currV.setConnInst(inst);
            // continue along same edge 
            startBlock = true;
        } else {
            vIList.append(inst);
        }
        InstructionHandle nextHandle = currHandle.getNext();
        if (nextHandle != null) {
            if (hasInbound(nextHandle)) {
                // This instruction is the target of a jump; start 
                // a new block.  Connector is set to flow by default. 
                startBlock = true;
            }
        }
        if (startBlock == true) {
            if (lineTab != null) {
                currV.setEndLine(lineTab.getSourceLine(0));
            }
        }
        currHandle = nextHandle;
        if (currHandle != null) {
            pos = currHandle.getPosition();
            inst = currHandle.getInstruction();
        }
    }
    return cfg;
}
