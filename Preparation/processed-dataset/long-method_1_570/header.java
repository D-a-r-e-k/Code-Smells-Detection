void method0() { 
private boolean _bReturn = false;
private int _ncss = 0;
// general counter  
private int _loc = 0;
private int _cyc = 1;
private int _localCases = 0;
private String _sName = "";
// name of last token  
private String _sParameter = "";
private String _sPackage = "";
private String _sClass = "";
private String _sFunction = "";
private int _functions = 0;
// number of functions in this class  
//private int     _topLevelClasses = 0;  
private int _classes = 0;
private int _classLevel = 0;
private int _anonClassCount = 1;
private int _jvdcLines = 0;
// added by SMS  
private int _jvdc = 0;
private boolean _bPrivate = true;
//false;        // added by SMS  
private boolean _bPublic = true;
// added by SMS  
/**
     * For each class the number of formal
     * comments in toplevel methods, constructors, inner
     * classes, and for the class itself are counted.
     * The top level comment has to be directly before
     * the class definition, not before the package or
     * import statement as it is often seen in source code
     * examples (at the beginning of your source files you
     * should instead put your copyright notice).
     */
private int _javadocs = 0;
// global javadocs  
private List /*<FunctionMetric>*/
_vFunctions = new ArrayList();
// holds the statistics for each method  
/** 
     * Metrics for each class/interface are stored in this
     * vector.
     */
private List /*<ObjectMetric>*/
_vClasses = new ArrayList();
private List _vImports = new ArrayList();
private Object[] _aoPackage = null;
private Map /*<String,PackageMetric>*/
_htPackage = new HashMap();
private PackageMetric _pPackageMetric;
private Token _tmpToken = null;
/** Argh, too much of a state machine. */
private Token _tmpResultToken = null;
/** Generated Token Manager. */
public JavaParser15DebugTokenManager token_source;
JavaCharStream jj_input_stream;
/** Current token. */
public Token token;
/** Next token. */
public Token jj_nt;
private int jj_ntk;
private Token jj_scanpos, jj_lastpos;
private int jj_la;
/** Whether we are looking ahead. */
private boolean jj_lookingAhead = false;
private boolean jj_semLA;
private final LookaheadSuccess jj_ls = new LookaheadSuccess();
private int trace_indent = 0;
private boolean trace_enabled = true;
}
