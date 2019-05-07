void method0() { 
//* 
private static final boolean DEBUG_RESULTS = false;
private static final boolean DEBUG = false;
/*/
    private static boolean DEBUG_RESULTS  = true;
    private static boolean DEBUG          = true;
    //*/
private final InstructionVisitor extraDeletedInstructionVisitor;
private final InstructionVisitor extraAddedInstructionVisitor;
private final PartialEvaluator partialEvaluator;
private final PartialEvaluator simplePartialEvaluator = new PartialEvaluator();
private final SideEffectInstructionChecker sideEffectInstructionChecker = new SideEffectInstructionChecker(true);
private final MyUnusedParameterSimplifier unusedParameterSimplifier = new MyUnusedParameterSimplifier();
private final MyProducerMarker producerMarker = new MyProducerMarker();
private final MyVariableInitializationMarker variableInitializationMarker = new MyVariableInitializationMarker();
private final MyStackConsistencyFixer stackConsistencyFixer = new MyStackConsistencyFixer();
private final CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor(false);
private boolean[][] variablesNecessaryAfter = new boolean[ClassConstants.TYPICAL_CODE_LENGTH][ClassConstants.TYPICAL_VARIABLES_SIZE];
private boolean[][] stacksNecessaryAfter = new boolean[ClassConstants.TYPICAL_CODE_LENGTH][ClassConstants.TYPICAL_STACK_SIZE];
private boolean[][] stacksSimplifiedBefore = new boolean[ClassConstants.TYPICAL_CODE_LENGTH][ClassConstants.TYPICAL_STACK_SIZE];
private boolean[] instructionsNecessary = new boolean[ClassConstants.TYPICAL_CODE_LENGTH];
private int maxMarkedOffset;
}
