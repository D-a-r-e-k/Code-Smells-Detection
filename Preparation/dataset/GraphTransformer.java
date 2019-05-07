/* GraphTransformer.java */
package org.quilt.cl;

import java.util.List;
import org.apache.bcel.classfile.LineNumberTable;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.*;

import org.quilt.graph.*;

/**
 * <p>Build the control flow graph for a method, apply an arbitrary
 * number of GraphXformers to it, and then collapse the graph
 * back to an instruction list.  Exception handlers are collected
 * from the control flow graph and made available to callers.</p>
 *
 * <p>XXX Debug statements should be removed when code is definitely
 * stable. XXX</p>
 *
 * @author <a href="mailto:jddixon@users.sourceforge.net">Jim Dixon</a>
 */
public class GraphTransformer {

    /** GraphXformer vector; ordered list of graph processors. */
    private List gxf;         

    private TryStacks ts = null;

    /** Exception handlers found in the method. */
    private CodeExceptionGen[] handlers;
    
    /** Exception handlers from the transformed graph. */
    private CodeExceptionGen [] ceg = null;

    /** Instruction list generated from the graph. */
    private InstructionList ilist = null;

    /** 
     * Creates method control flow graphs, applies application-
     * specific transforms, and then collapses the graph to produce
     * a new instruction list.
     * 
     * @param gxf List of application-specific graph transformers. 
     *
     * @author <a href="jddixon@users.sourceforge.net">Jim Dixon</a>
     */
    public GraphTransformer ( List gxf ) {
        this.gxf = gxf;
    }

    private void zapGraphXformer ( GraphXformer gxf, Exception e) {
        System.err.println("WARNING: exception in " 
            // + gxf.getName() 
            + ": transformation will not be applied" );
        e.printStackTrace();
        gxf = null;
    }
    /**
     * Build a control flow graph for a method's instruction list,
     * apply any transformers to it, and collapse the graph into
     * a new instruction list.
     *
     * @param clazz  Class being transformed.
     * @param method The method being transformed.
     * @return   The transformed instruction list.
     */
    public InstructionList xform (ClassGen clazz, MethodGen method )  {
        if (method == null) {
            throw new IllegalArgumentException("null method");
        }
        ControlFlowGraph graph = makeGraph(clazz, method); 
        GraphXformer [] xf = new GraphXformer[ gxf.size() ];
       
        // apply each graph processor in turn
        for (int i = 0; i < gxf.size(); i++) {
            try { 
                xf[i] = (GraphXformer) (
                        (gxf.get(i)).getClass().newInstance() );
            } catch (IllegalAccessException e ) {
                zapGraphXformer (xf[i], e);
            } catch (InstantiationException e ) {
                zapGraphXformer (xf[i], e);
            }
            if (xf[i] != null && graph != null) {
                xf[i].xform(clazz, method, graph);
            }
        }
       
        if (graph == null) {
            return null; 
        }
        BytecodeCollector bc  = collapseGraph(graph);
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
                System.out.println( 
    "GraphTransformer.xform WARNING - PROBABLE INTERNAL ERROR:\n   method had " 
                    + handlers.length 
                    + " exception handlers, but after graph transformation " 
                    + ceg.length);
            }                
        }
        return ilist;
    }
    /** 
     * Returns array of exception handlers, empty if the instruction
     * list was null or there were no exception handlers.
     * 
     * @return   Array of CodeExceptionGen.
     */
    public CodeExceptionGen[] getExceptionHandlers() {
        if (ilist != null && ceg != null) {
            return ceg;
        } else {
            return new CodeExceptionGen[0];
        }
    }
    /**
     * Build the graph for the method.
     * 
     * @param clazz Class being transformed in bcel ClassGen. 
     * @param m  BCEL representation of method we are working on.
     */
    final protected ControlFlowGraph makeGraph ( ClassGen clazz, 
                                                    MethodGen method ) {
    
        SortedBlocks blox           = new SortedBlocks();
        handlers                    = method.getExceptionHandlers();
        ControlFlowGraph cfg        = new ControlFlowGraph();
        ControlFlowGraph currGraph  = cfg;
        Edge e                      = cfg.getEntry().getEdge();
        ts                          = null;
        boolean startBlock          = false;
        CodeVertex currV            = null;
        LineNumberTable lineTab     = method.getLineNumberTable (
                                                clazz.getConstantPool() );
        if (handlers.length > 0) {
            // NEED TO ADJUST EDGE HERE 
            ts = new TryStacks (handlers, blox, cfg);
        }
        if (blox.exists(0)) {
            // we must have a try block starting at 0
            currV = blox.get(0);
        } else {
            currV = blox.find (0, currGraph, e);
        }
        if (lineTab != null) {
            currV.setStartLine( lineTab.getSourceLine(0) );
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
                    if ( !blox.exists(pos) ) {
                        // XXX this was formerly regarded as an
                        // error; handling it like this makes it
                        // clearer what the problem is, but we now get
                        //     Falling off the end of the code
                        currV = new CodeVertex (currGraph, pos);
                    } else {
                        currV = blox.get(pos); 
                    }
                } else {
                    currV = blox.find(pos, currGraph, e);
                }
                if (lineTab != null) {
                    currV.setStartLine( lineTab.getSourceLine(pos) );
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
                int tpos = ((GotoInstruction)inst).getTarget().getPosition();
                int endTry;
                if (ts == null) {
                    endTry = -1;
                } else {
                    endTry= ts.getEndTry(currGraph);
                }
                if (endTry >= 0 && tpos > endTry) {
                    // tpos is beyond end of try block and should be the
                    // first code vertex following the subgraph Exit; in 
                    // any case the edge target becomes the Exit
                    Exit currExit = currGraph.getExit();
                    otherEdge.setTarget(currExit);
                    if (!blox.exists(tpos)) {
                        Vertex vFinal;
                        for (vFinal = currExit; 
                                        vFinal.getTarget() instanceof Entry; 
                                            vFinal = vFinal.getTarget()) {
                            ;
                        }
                        blox.add (tpos, vFinal.getEdge());
                    }
                } else {
                    // tpos is within try block; make v target of e
                    blox.find (tpos, currGraph, otherEdge); 
                }
                // continue to use this 'virtual' edge
                startBlock = true;
//              // DEBUG
//              System.out.println("GraphTransformer: goto at end of " 
//                  + currV);
//              // END

            } else if (inst instanceof IfInstruction 
                    || inst instanceof JSR           ) {
                Edge otherEdge = currV.makeBinary();
                currV.setConnInst(inst);
                // handle 'then' branch or target of JSR
                int tpos = ((BranchInstruction)inst).getTarget().getPosition();
                //  edge for 'then' vertex
                blox.find(tpos, currGraph, otherEdge);
                // continue to use the current edge
                startBlock = true;          // ... but start a new block
                
            } else if (inst instanceof ReturnInstruction
                    || inst instanceof RET) {
                currV.setConnInst(inst);
                e = null;
                startBlock = true;

            } else if (inst instanceof InvokeInstruction) {
                currV.setConnInst(inst);
                // continue to use the current edge
                startBlock = true;          // ... but start a new block
                
            } else if (inst instanceof Select) {
                InstructionHandle[] targets = ((Select)inst).getTargets();
                //MultiConnector conn = currV.makeMulti(targets.length);
                ComplexConnector conn = currV.makeComplex(targets.length);
                currV.setConnInst(inst);
                for (int i = 0; i < targets.length; i++) {
                    int tpos = targets[i].getPosition();
                    blox.find(tpos, currGraph, conn.getEdge(i));
                }
                // EXPERIMENT IN HANDLING THE DEFAULT - seems to work ...
                InstructionHandle theDefault = ((Select)inst).getTarget();
                if (theDefault != null) {
                    blox.find ( theDefault.getPosition(), 
                                                currGraph, conn.getEdge());
                }
                e = null;   // it's an n-way goto
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
                vIList.append(inst);            // add the instruction
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
                    currV.setEndLine( lineTab.getSourceLine(0) );
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
    /** 
     * Whether this instruction is target of branch instruction or
     * starts catch block.
     * 
     * @param ih Handle on instruction.
     * @return   True if targeted by branch or CodeExceptionGen.
     */
    final public static boolean hasInbound (InstructionHandle ih ) {
        if (ih.hasTargeters()) {
            InstructionTargeter targeters[] = ih.getTargeters();
            for (int j = 0; j < targeters.length; j++) {
                if (targeters[j] instanceof BranchInstruction) {
                    return true;
                }
                if (targeters[j] instanceof CodeExceptionGen) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Collapse a method control flow graph, writing bytecode back
     * into the MethodGen data structures.
     */
    final protected BytecodeCollector collapseGraph(ControlFlowGraph graph) {
        BytecodeCollector theMan = new BytecodeCollector();
        new Walker().visit(graph, theMan);
        return theMan;
    } 
}
