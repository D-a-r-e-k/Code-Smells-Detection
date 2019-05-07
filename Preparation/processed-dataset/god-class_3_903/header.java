void method0() { 
public static boolean unicodeWarningGiven = false;
public static int generatedStates = 0;
private static int idCnt = 0;
private static int lohiByteCnt;
private static int dummyStateIndex = -1;
private static boolean done;
private static boolean mark[];
private static boolean stateDone[];
private static List allStates = new ArrayList();
private static List indexedAllStates = new ArrayList();
private static List nonAsciiTableForMethod = new ArrayList();
private static Hashtable equivStatesTable = new Hashtable();
private static Hashtable allNextStates = new Hashtable();
private static Hashtable lohiByteTab = new Hashtable();
private static Hashtable stateNameForComposite = new Hashtable();
private static Hashtable compositeStateTable = new Hashtable();
private static Hashtable stateBlockTable = new Hashtable();
private static Hashtable stateSetsToFix = new Hashtable();
private static boolean jjCheckNAddStatesUnaryNeeded = false;
private static boolean jjCheckNAddStatesDualNeeded = false;
long[] asciiMoves = new long[2];
char[] charMoves = null;
private char[] rangeMoves = null;
NfaState next = null;
private NfaState stateForCase;
Vector epsilonMoves = new Vector();
private String epsilonMovesString;
private NfaState[] epsilonMoveArray;
private int id;
int stateName = -1;
int kind = Integer.MAX_VALUE;
private int lookingFor;
private int usefulEpsilonMoves = 0;
int inNextOf;
private int lexState;
private int nonAsciiMethod = -1;
private int kindToPrint = Integer.MAX_VALUE;
boolean dummy = false;
private boolean isComposite = false;
private int[] compositeStates = null;
boolean isFinal = false;
private Vector loByteVec;
private int[] nonAsciiMoveIndices;
private int round = 0;
private int onlyChar = 0;
private char matchSingleChar;
private boolean closureDone = false;
static List allBitVectors = new ArrayList();
/* This function generates the bit vectors of low and hi bytes for common
      bit vectors and returns those that are not common with anything (in
      loBytes) and returns an array of indices that can be used to generate
      the function names for char matching using the common bit vectors.
      It also generates code to match a char with the common bit vectors.
      (Need a better comment). */
static int[] tmpIndices = new int[512];
static String allBits = "{\n   0xffffffffffffffffL, " + "0xffffffffffffffffL, " + "0xffffffffffffffffL, " + "0xffffffffffffffffL\n};";
static Hashtable tableToDump = new Hashtable();
static List orderedStateSet = new ArrayList();
static int lastIndex = 0;
static int[][] kinds;
static int[][][] statesForState;
}
