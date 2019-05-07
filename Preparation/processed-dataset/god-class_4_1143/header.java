void method0() { 
// REVISIT: only local element and attribute are different from others.  
//          it's possible to have either name or ref. all the others  
//          are only allowed to have one of name or ref, or neither of them.  
//          we'd better move such checking to the traverser.  
private static final String ELEMENT_N = "element_n";
private static final String ELEMENT_R = "element_r";
private static final String ATTRIBUTE_N = "attribute_n";
private static final String ATTRIBUTE_R = "attribute_r";
private static int ATTIDX_COUNT = 0;
public static final int ATTIDX_ABSTRACT = ATTIDX_COUNT++;
public static final int ATTIDX_AFORMDEFAULT = ATTIDX_COUNT++;
public static final int ATTIDX_BASE = ATTIDX_COUNT++;
public static final int ATTIDX_BLOCK = ATTIDX_COUNT++;
public static final int ATTIDX_BLOCKDEFAULT = ATTIDX_COUNT++;
public static final int ATTIDX_DEFAULT = ATTIDX_COUNT++;
public static final int ATTIDX_EFORMDEFAULT = ATTIDX_COUNT++;
public static final int ATTIDX_FINAL = ATTIDX_COUNT++;
public static final int ATTIDX_FINALDEFAULT = ATTIDX_COUNT++;
public static final int ATTIDX_FIXED = ATTIDX_COUNT++;
public static final int ATTIDX_FORM = ATTIDX_COUNT++;
public static final int ATTIDX_ID = ATTIDX_COUNT++;
public static final int ATTIDX_ITEMTYPE = ATTIDX_COUNT++;
public static final int ATTIDX_MAXOCCURS = ATTIDX_COUNT++;
public static final int ATTIDX_MEMBERTYPES = ATTIDX_COUNT++;
public static final int ATTIDX_MINOCCURS = ATTIDX_COUNT++;
public static final int ATTIDX_MIXED = ATTIDX_COUNT++;
public static final int ATTIDX_NAME = ATTIDX_COUNT++;
public static final int ATTIDX_NAMESPACE = ATTIDX_COUNT++;
public static final int ATTIDX_NAMESPACE_LIST = ATTIDX_COUNT++;
public static final int ATTIDX_NILLABLE = ATTIDX_COUNT++;
public static final int ATTIDX_NONSCHEMA = ATTIDX_COUNT++;
public static final int ATTIDX_PROCESSCONTENTS = ATTIDX_COUNT++;
public static final int ATTIDX_PUBLIC = ATTIDX_COUNT++;
public static final int ATTIDX_REF = ATTIDX_COUNT++;
public static final int ATTIDX_REFER = ATTIDX_COUNT++;
public static final int ATTIDX_SCHEMALOCATION = ATTIDX_COUNT++;
public static final int ATTIDX_SOURCE = ATTIDX_COUNT++;
public static final int ATTIDX_SUBSGROUP = ATTIDX_COUNT++;
public static final int ATTIDX_SYSTEM = ATTIDX_COUNT++;
public static final int ATTIDX_TARGETNAMESPACE = ATTIDX_COUNT++;
public static final int ATTIDX_TYPE = ATTIDX_COUNT++;
public static final int ATTIDX_USE = ATTIDX_COUNT++;
public static final int ATTIDX_VALUE = ATTIDX_COUNT++;
public static final int ATTIDX_ENUMNSDECLS = ATTIDX_COUNT++;
public static final int ATTIDX_VERSION = ATTIDX_COUNT++;
public static final int ATTIDX_XML_LANG = ATTIDX_COUNT++;
public static final int ATTIDX_XPATH = ATTIDX_COUNT++;
public static final int ATTIDX_FROMDEFAULT = ATTIDX_COUNT++;
//public static final int ATTIDX_OTHERVALUES     = ATTIDX_COUNT++;  
public static final int ATTIDX_ISRETURNED = ATTIDX_COUNT++;
private static final XIntPool fXIntPool = new XIntPool();
// constants to return  
private static final XInt INT_QUALIFIED = fXIntPool.getXInt(SchemaSymbols.FORM_QUALIFIED);
private static final XInt INT_UNQUALIFIED = fXIntPool.getXInt(SchemaSymbols.FORM_UNQUALIFIED);
private static final XInt INT_EMPTY_SET = fXIntPool.getXInt(XSConstants.DERIVATION_NONE);
private static final XInt INT_ANY_STRICT = fXIntPool.getXInt(XSWildcardDecl.PC_STRICT);
private static final XInt INT_ANY_LAX = fXIntPool.getXInt(XSWildcardDecl.PC_LAX);
private static final XInt INT_ANY_SKIP = fXIntPool.getXInt(XSWildcardDecl.PC_SKIP);
private static final XInt INT_ANY_ANY = fXIntPool.getXInt(XSWildcardDecl.NSCONSTRAINT_ANY);
private static final XInt INT_ANY_LIST = fXIntPool.getXInt(XSWildcardDecl.NSCONSTRAINT_LIST);
private static final XInt INT_ANY_NOT = fXIntPool.getXInt(XSWildcardDecl.NSCONSTRAINT_NOT);
private static final XInt INT_USE_OPTIONAL = fXIntPool.getXInt(SchemaSymbols.USE_OPTIONAL);
private static final XInt INT_USE_REQUIRED = fXIntPool.getXInt(SchemaSymbols.USE_REQUIRED);
private static final XInt INT_USE_PROHIBITED = fXIntPool.getXInt(SchemaSymbols.USE_PROHIBITED);
private static final XInt INT_WS_PRESERVE = fXIntPool.getXInt(XSSimpleType.WS_PRESERVE);
private static final XInt INT_WS_REPLACE = fXIntPool.getXInt(XSSimpleType.WS_REPLACE);
private static final XInt INT_WS_COLLAPSE = fXIntPool.getXInt(XSSimpleType.WS_COLLAPSE);
private static final XInt INT_UNBOUNDED = fXIntPool.getXInt(SchemaSymbols.OCCURRENCE_UNBOUNDED);
// used to store the map from element name to attribute list  
// for 14 global elements  
private static final Hashtable fEleAttrsMapG = new Hashtable(29);
// for 39 local elememnts  
private static final Hashtable fEleAttrsMapL = new Hashtable(79);
// used to initialize fEleAttrsMap  
// step 1: all possible data types  
// DT_??? >= 0 : validate using a validator, which is initialized staticly  
// DT_??? <  0 : validate directly, which is done in "validate()"  
protected static final int DT_ANYURI = 0;
protected static final int DT_ID = 1;
protected static final int DT_QNAME = 2;
protected static final int DT_STRING = 3;
protected static final int DT_TOKEN = 4;
protected static final int DT_NCNAME = 5;
protected static final int DT_XPATH = 6;
protected static final int DT_XPATH1 = 7;
protected static final int DT_LANGUAGE = 8;
// used to store extra datatype validators  
protected static final int DT_COUNT = DT_LANGUAGE + 1;
private static final XSSimpleType[] fExtraDVs = new XSSimpleType[DT_COUNT];
protected static final int DT_BLOCK = -1;
protected static final int DT_BLOCK1 = -2;
protected static final int DT_FINAL = -3;
protected static final int DT_FINAL1 = -4;
protected static final int DT_FINAL2 = -5;
protected static final int DT_FORM = -6;
protected static final int DT_MAXOCCURS = -7;
protected static final int DT_MAXOCCURS1 = -8;
protected static final int DT_MEMBERTYPES = -9;
protected static final int DT_MINOCCURS1 = -10;
protected static final int DT_NAMESPACE = -11;
protected static final int DT_PROCESSCONTENTS = -12;
protected static final int DT_USE = -13;
protected static final int DT_WHITESPACE = -14;
protected static final int DT_BOOLEAN = -15;
protected static final int DT_NONNEGINT = -16;
protected static final int DT_POSINT = -17;
// used to resolver namespace prefixes  
protected XSDHandler fSchemaHandler = null;
// used to store symbols.  
protected SymbolTable fSymbolTable = null;
// used to store the mapping from processed element to attributes  
protected Hashtable fNonSchemaAttrs = new Hashtable();
// temprory vector, used to hold the namespace list  
protected Vector fNamespaceList = new Vector();
// whether this attribute appeared in the current element  
protected boolean[] fSeen = new boolean[ATTIDX_COUNT];
private static boolean[] fSeenTemp = new boolean[ATTIDX_COUNT];
// the following part implements an attribute-value-array pool.  
// when checkAttribute is called, it calls getAvailableArray to get  
// an array from the pool; when the caller is done with the array,  
// it calls returnAttrArray to return that array to the pool.  
// initial size of the array pool. 10 is big enough  
static final int INIT_POOL_SIZE = 10;
// the incremental size of the array pool  
static final int INC_POOL_SIZE = 10;
// the array pool  
Object[][] fArrayPool = new Object[INIT_POOL_SIZE][ATTIDX_COUNT];
// used to clear the returned array  
// I think System.arrayCopy is more efficient than setting 35 fields to null  
private static Object[] fTempArray = new Object[ATTIDX_COUNT];
// current position of the array pool (# of arrays not returned)  
int fPoolPos = 0;
}
