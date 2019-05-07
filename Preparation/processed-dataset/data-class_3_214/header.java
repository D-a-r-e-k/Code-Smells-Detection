void method0() { 
//* 
private static final boolean DEBUG = false;
private static final boolean DEBUG_RESULTS = false;
/*/
    private static boolean DEBUG         = true;
    private static boolean DEBUG_RESULTS = true;
    //*/
private static final int MAXIMUM_EVALUATION_COUNT = 5;
public static final int NONE = -2;
public static final int AT_METHOD_ENTRY = -1;
public static final int AT_CATCH_ENTRY = -1;
private final ValueFactory valueFactory;
private final InvocationUnit invocationUnit;
private final boolean evaluateAllCode;
private InstructionOffsetValue[] branchOriginValues = new InstructionOffsetValue[ClassConstants.TYPICAL_CODE_LENGTH];
private InstructionOffsetValue[] branchTargetValues = new InstructionOffsetValue[ClassConstants.TYPICAL_CODE_LENGTH];
private TracedVariables[] variablesBefore = new TracedVariables[ClassConstants.TYPICAL_CODE_LENGTH];
private TracedStack[] stacksBefore = new TracedStack[ClassConstants.TYPICAL_CODE_LENGTH];
private TracedVariables[] variablesAfter = new TracedVariables[ClassConstants.TYPICAL_CODE_LENGTH];
private TracedStack[] stacksAfter = new TracedStack[ClassConstants.TYPICAL_CODE_LENGTH];
private boolean[] generalizedContexts = new boolean[ClassConstants.TYPICAL_CODE_LENGTH];
private int[] evaluationCounts = new int[ClassConstants.TYPICAL_CODE_LENGTH];
private boolean evaluateExceptions;
private final BasicBranchUnit branchUnit;
private final BranchTargetFinder branchTargetFinder;
private final java.util.Stack callingInstructionBlockStack;
private final java.util.Stack instructionBlockStack = new java.util.Stack();
}
