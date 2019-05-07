void method0() { 
//  
// Constants  
//  
private static final boolean DEBUG = false;
// special strings  
// debugging  
/** Set to true to debug content model validation. */
private static final boolean DEBUG_VALIDATE_CONTENT = false;
//  
// Data  
//  
/**
     * This is the map of unique input symbol elements to indices into
     * each state's per-input symbol transition table entry. This is part
     * of the built DFA information that must be kept around to do the
     * actual validation.  Note tat since either XSElementDecl or XSParticleDecl object
     * can live here, we've got to use an Object.
     */
private Object fElemMap[] = null;
/**
     * This is a map of whether the element map contains information
     * related to ANY models.
     */
private int fElemMapType[] = null;
/**
     * id of the unique input symbol
     */
private int fElemMapId[] = null;
/** The element map size. */
private int fElemMapSize = 0;
/**
     * This is an array of booleans, one per state (there are
     * fTransTableSize states in the DFA) that indicates whether that
     * state is a final state.
     */
private boolean fFinalStateFlags[] = null;
/**
     * The list of follow positions for each NFA position (i.e. for each
     * non-epsilon leaf node.) This is only used during the building of
     * the DFA, and is let go afterwards.
     */
private CMStateSet fFollowList[] = null;
/**
     * This is the head node of our intermediate representation. It is
     * only non-null during the building of the DFA (just so that it
     * does not have to be passed all around.) Once the DFA is built,
     * this is no longer required so its nulled out.
     */
private CMNode fHeadNode = null;
/**
     * The count of leaf nodes. This is an important number that set some
     * limits on the sizes of data structures in the DFA process.
     */
private int fLeafCount = 0;
/**
     * An array of non-epsilon leaf nodes, which is used during the DFA
     * build operation, then dropped.
     */
private XSCMLeaf fLeafList[] = null;
/** Array mapping ANY types to the leaf list. */
private int fLeafListType[] = null;
/**
     * This is the transition table that is the main by product of all
     * of the effort here. It is an array of arrays of ints. The first
     * dimension is the number of states we end up with in the DFA. The
     * second dimensions is the number of unique elements in the content
     * model (fElemMapSize). Each entry in the second dimension indicates
     * the new state given that input for the first dimension's start
     * state.
     * <p>
     * The fElemMap array handles mapping from element indexes to
     * positions in the second dimension of the transition table.
     */
private int fTransTable[][] = null;
/**
     * Array containing occurence information for looping states 
     * which use counters to check minOccurs/maxOccurs.
     */
private Occurence[] fCountingStates = null;
/**
     * The number of valid entries in the transition table, and in the other
     * related tables such as fFinalStateFlags.
     */
private int fTransTableSize = 0;
private boolean fIsCompactedForUPA;
private static long time = 0;
}
