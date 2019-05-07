void method0() { 
private final boolean m_excludeSyntheticMethods;
private final boolean m_excludeBridgeMethods;
private final boolean m_doSUIDCompensation;
private final Logger m_log;
// instr visitor logging context is latched at construction time  
// non-resettable state:  
private boolean m_warningIssued;
// resettable state:  
private boolean m_instrument;
private boolean m_metadata;
private boolean m_ignoreAlreadyInstrumented;
/*private*/
ClassDef m_cls;
private String m_classPackageName;
// in JVM format [com/vladium/...]; empty string for default package  
private String m_className;
// in JVM format [<init>, <clinit>, etc], relative to 'm_classPackageName'  
private String m_classSrcFileName;
private int[][][] m_classBlockMetadata;
// methodID->(blockID->line) map [valid only if 'm_constructMetadata' is true; null if the method has not line number table]  
private MethodDescriptor[] m_classMethodDescriptors;
// current class scope:   
private int m_syntheticStringIndex;
// index of CONSTANT_Utf8 String that reads "Synthetic"       
/*private*/
int m_coverageFieldrefIndex;
// index of the Fieldref for COVERAGE_FIELD  
private int m_registerMethodrefIndex;
// index of Methodref for RT.r()  
/*private*/
int m_preclinitMethodrefIndex;
// index of Methodref for pre-<clinit> method  
/*private*/
int m_classNameConstantIndex;
// index of CONSTANT_String that is the class name [in JVM format]  
private int m_stampIndex;
// index of CONSTANT_Long that is the class instr stamp  
private int m_clinitID;
// offset of <clinit> method [-1 if not determined yet]  
private int m_clinitStatus;
/*private*/
int m_classInstrMethodCount;
// the number of slots in 'm_classBlockCounts' corresponding to methods to be instrumented for coverage  
/*private*/
int[] m_classBlockCounts;
// basic block counts for all methods [only valid just before <clinit> is processed]  
private long m_classSignature;
// current method scope:   
/*private*/
int m_methodID;
// offset of current method being instrumented  
private String m_methodName;
private int m_methodFirstLine;
private int[] m_methodBlockOffsets;
// [unadjusted] basic block boundaries [length = m_classBlockCounts[m_methodID]+1; the last slot is method bytecode length]  
private int[] m_methodBlockSizes;
private int[] m_methodJumpAdjOffsets;
// TODO: length ?  
private int[] m_methodJumpAdjValues;
// TODO: length ?  
private static final long NBEAST = 16661;
// prime  
private static final String COVERAGE_FIELD_NAME = "$VR" + "c";
private static final String SUID_FIELD_NAME = "serialVersionUID";
private static final String PRECLINIT_METHOD_NAME = "$VR" + "i";
private static final String JAVA_IO_SERIALIZABLE_NAME = "java/io/Serializable";
private static final String JAVA_IO_EXTERNALIZABLE_NAME = "java/io/Externalizable";
private static final int EMIT_CTX_MIN_INIT_CAPACITY = 64;
// good value determined empirically  
private static final int PRECLINIT_INIT_CAPACITY = 128;
// covers about 80% of classes (no reallocation)  
private static final boolean MARK_ADDED_ELEMENTS_SYNTHETIC = true;
/* It appears that nested classes and interfaces ought to be marked
     * as Synthetic; however, neither Sun nor IBM compilers seem to do this.
     * 
     * (As a side note, implied no-arg constructors ought to be marked as
     * synthetic as well, but Sun's javac is not consistent about that either)  
     */
private static final boolean SKIP_SYNTHETIC_CLASSES = false;
private static final LineNumberComparator LINE_NUMBER_COMPARATOR = new LineNumberComparator();
private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
}
