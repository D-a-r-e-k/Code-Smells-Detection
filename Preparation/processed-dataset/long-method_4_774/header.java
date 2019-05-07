void method0() { 
static final boolean DEBUG = SystemProperties.getBoolean("inva.debug");
private final MethodGen methodGen;
private final IsNullValueFrameModelingVisitor visitor;
private final ValueNumberDataflow vnaDataflow;
private final TypeDataflow typeDataflow;
private final CFG cfg;
private Set<LocationWhereValueBecomesNull> locationWhereValueBecomesNullSet;
private final boolean trackValueNumbers;
private IsNullValueFrame lastFrame;
private IsNullValueFrame instanceOfFrame;
private IsNullValueFrame cachedEntryFact;
private JavaClassAndMethod classAndMethod;
@CheckForNull
private final PointerEqualityCheck pointerEqualityCheck;
private static final BitSet nullComparisonInstructionSet = new BitSet();
}
