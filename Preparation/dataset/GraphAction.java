/* GraphAction.java */
package org.quilt.cover.stmt;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.generic.*;
import org.quilt.cl.*;
import org.quilt.graph.*;

/**
 * Walk the control flow graph, adding counter vertices on all edges
 * inbound to code vertices.  XXX Except those likely to cause 
 * problems; need to review this code later to make sure that all
 * possibilities are covered.
 *
 * @author <a href="mailto:jddixon@users.sourceforge.net">Jim Dixon</a>
 */
public class GraphAction implements GraphXformer {

    private static StmtRegistry  stmtReg  = null;
    private ClassGen      clazz_   = null;
    private MethodGen     method_  = null;

    /** Processor name for use in reports. */
    private static String name_ = null;

    private InstructionFactory factory_;

    private ConstantPoolGen cpGen_;

    /** Number of counters added to the graph. */
    private int counterCount = 0;
   
    /** The class Xformer responsible for tracking counter counts */
    private ClassAction classAct_ = null;
    
    /** constructor. */
    public GraphAction () {}

    public GraphAction (StmtRegistry reg) {
        stmtReg = reg;
        setName(this.getClass().getName());
    }

    /** 
     * Constructor used by StmtRegistry. XXX Deprecate, dropping 
     * classAct argument. (Pass via registry if it's really necessary.)
     *
     * @param classAct The Xformer at the class level; the method needs
     *                 to report back to it.
     */
    public GraphAction (StmtRegistry reg, ClassAction classAct) {
        if (reg == null || classAct == null) {
            throw new IllegalArgumentException("null argument");
        }
        stmtReg   = reg;
        classAct_ = classAct;
        setName(this.getClass().getName());
    }
    /** The visitor that actually adds the vertices to the graph. */
    private class LampLighter implements org.quilt.graph.Visitor, 
                            org.apache.bcel.Constants, 
                            org.apache.bcel.generic.InstructionConstants {
        private ControlFlowGraph graph = null;
        public  LampLighter() {}
        public void discoverGraph(Directed g) {
            graph = (ControlFlowGraph)g;
        }
        public void discoverVertex(Vertex v) {
        }
        /**
         * Instrument edges inbound to code vertex.
         */
        public void discoverEdge(Edge e) {
            Vertex source = e.getSource();
            Vertex target = e.getTarget();
            ControlFlowGraph srcGraph = (ControlFlowGraph)source.getGraph();
            ControlFlowGraph tgtGraph = (ControlFlowGraph)target.getGraph();
            // DEBUG
            if (tgtGraph != graph) {
                System.out.println("GraphAction.discoverEdge:\n"
                    + "  current graph is " + graph.getIndex() 
                    + " but edge is " + e);
            }
            // END
            boolean addingCounter = true;
            if ( (target instanceof CodeVertex)
             && !(target instanceof CounterVertex)
             && !(source instanceof CounterVertex) ) {
                Instruction srcConnInst = null;
                if (source instanceof CodeVertex) {
                    srcConnInst = ((CodeVertex)source).getConnInst();
                    if (srcConnInst != null 
                            && srcConnInst instanceof GotoInstruction
                            && (source.getGraph() != tgtGraph)) {
                        // Don't add a counter if the goto jumps out of 
                        // this graph.  XXX needs more thought! -- this
                        // eliminates some test failures but will fail to
                        // count some statement hits
                        addingCounter = false;
                    }
                }
                // XXX A BIT OF A KLUDGE (NestedTryBlocks bugfix)
                if (srcGraph != graph) {
                    System.out.println("GraphAction.discoverEdge WARNING: "
                        + "graph index " + graph.getIndex() 
                        + "\n    but edge is " + e
                        + " - not adding counter");
                    addingCounter = false;
                }
                if (addingCounter) {
                    CounterVertex cv;
                    if (source instanceof Exit) {
                        // MOVED UP
//                      ControlFlowGraph tgtGraph
//                                  = (ControlFlowGraph)target.getGraph();
                        cv = (CounterVertex) tgtGraph.insertCodeVertex(
                                (CodeVertex)new CounterVertex(tgtGraph), e);
    
                    } else {
                        cv = (CounterVertex) graph.insertCodeVertex(
                                (CodeVertex)new CounterVertex(graph), e);
                    }
                    InstructionList ilist = cv.getInstructionList();
                    InstructionHandle ih = ilist.append (
                        factory_.createFieldAccess(clazz_.getClassName(), "q$$q",
                            new ArrayType(Type.INT,1), Constants.GETSTATIC));
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
                                if (!( e == source.getEdge() )) {
                                    ((CodeVertex)source).moveGoto(cv);
                                }
                            } else if (srcConnInst instanceof IfInstruction
                                    || srcConnInst instanceof JsrInstruction ) {
                                Edge otherEdge 
                                    = ((BinaryConnector)source.getConnector())
                                    .getOtherEdge();
                                if (e == otherEdge) {
                                    // this is the 'else' branch; the 
                                    // counter needs to have a Goto
                                    // connecting instruction
                                    BranchInstruction bi 
                                        = (BranchInstruction) srcConnInst;
                                    InstructionHandle targ = bi.getTarget();
                                    bi.setTarget(ih);
                                    cv.setConnInst( new GOTO (targ) );
                                    // ignore the edge returned
                                    cv.makeBinary();
                                    BinaryConnector bc = (BinaryConnector)
                                                        cv.getConnector();
                                    Vertex myTarget = bc.getTarget();
                                    // main edge is the flow-through exit
                                    // DEBUG
                                    if (graph.getExit() == null) {
                                        System.out.println(
                                                "GRAPH HAS NULL EXIT");
                                    }
                                    // END
                                    bc.setTarget(graph.getExit());
                                    bc.setOtherTarget(myTarget);
                                }
                            }
                            // COMPLEX - Select /////////////////////////
    
                        } // if srcConnInst != null
                    }
                } // GEEP
                // ELSE IF ENTRY ... ////////////////////////////////
            }
        }
        public void finishEdge(Edge e) {
        }
        public void finishVertex(Vertex v) {
        }
        public void finishGraph(Directed g) {
        }
    }
    // Apply the transformation to the graph. 
    public void xform (final ClassGen cg, final MethodGen method, 
                                          final ControlFlowGraph cfg) {
        clazz_   = cg;
        method_  = method;
        cpGen_   = cg.getConstantPool() ;
        factory_ = new InstructionFactory (clazz_, cpGen_);

        String className = clazz_.getClassName();
        Ephemera eph = stmtReg.getEphemera(className);
        // DEBUG
        if (eph == null)
            System.out.println("GraphAction.xform: eph is null!");
        // END
        counterCount = eph.getCounterCount();

        int cfgSize = cfg.size();
        Walker walker = new Walker();
        walker.visit(cfg, new LampLighter());

        // unnecessary, I think, because the ConstantPoolGen is part
        // of the ClassGen
        // clazz_.setConstantPool(cpGen_.getFinalConstantPool());

        eph.setEndCount(method.getName(), counterCount);


    }
    // GET/SET METHODS //////////////////////////////////////////////
    /** Get the name for the transformation. */
    public static String getName() {
        return name_;
    }
    /**
     * Set a name for the transformation, to allow reports to refer
     * to it.
     */
    public static void setName(String name) {
        name_ = name;
    }
}
