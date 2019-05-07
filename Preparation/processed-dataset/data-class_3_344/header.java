void method0() { 
// Name of this function call  
private QName _fname;
// Arguments to this function call (might not be any)  
private final Vector _arguments;
// Empty argument list, used for certain functions  
private static final Vector EMPTY_ARG_LIST = new Vector(0);
// Valid namespaces for Java function-call extension  
protected static final String EXT_XSLTC = TRANSLET_URI;
protected static final String JAVA_EXT_XSLTC = EXT_XSLTC + "/java";
protected static final String EXT_XALAN = "http://xml.apache.org/xalan";
protected static final String JAVA_EXT_XALAN = "http://xml.apache.org/xalan/java";
protected static final String JAVA_EXT_XALAN_OLD = "http://xml.apache.org/xslt/java";
protected static final String EXSLT_COMMON = "http://exslt.org/common";
protected static final String EXSLT_MATH = "http://exslt.org/math";
protected static final String EXSLT_SETS = "http://exslt.org/sets";
protected static final String EXSLT_DATETIME = "http://exslt.org/dates-and-times";
protected static final String EXSLT_STRINGS = "http://exslt.org/strings";
// Namespace format constants  
protected static final int NAMESPACE_FORMAT_JAVA = 0;
protected static final int NAMESPACE_FORMAT_CLASS = 1;
protected static final int NAMESPACE_FORMAT_PACKAGE = 2;
protected static final int NAMESPACE_FORMAT_CLASS_OR_PACKAGE = 3;
// Namespace format  
private int _namespace_format = NAMESPACE_FORMAT_JAVA;
/**
     * Stores reference to object for non-static Java calls
     */
Expression _thisArgument = null;
// External Java function's class/method/signature  
private String _className;
private Class _clazz;
private Method _chosenMethod;
private Constructor _chosenConstructor;
private MethodType _chosenMethodType;
// Encapsulates all unsupported external function calls  
private boolean unresolvedExternal;
// If FunctionCall is a external java constructor   
private boolean _isExtConstructor = false;
// If the java method is static  
private boolean _isStatic = false;
// Legal conversions between internal and Java types.  
private static final MultiHashtable _internal2Java = new MultiHashtable();
// Legal conversions between Java and internal types.  
private static final Hashtable _java2Internal = new Hashtable();
// The mappings between EXSLT extension namespaces and implementation classes  
private static final Hashtable _extensionNamespaceTable = new Hashtable();
// Extension functions that are implemented in BasisLibrary  
private static final Hashtable _extensionFunctionTable = new Hashtable();
}
