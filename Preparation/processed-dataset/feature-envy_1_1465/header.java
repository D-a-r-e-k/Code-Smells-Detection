void method0() { 
private static final String REGEX_OPTION = "-regex";
private static final String VERBOSE_OPTION = "-verbose";
public static final String STACK_TRACE_EXPRESSION = "(?:\\s*%c:.*)|(?:\\s*at\\s+%c.%m\\s*\\(.*?(?::%l)?\\)\\s*)";
private static final String REGEX_CLASS = "\\b(?:[A-Za-z0-9_$]+\\.)*[A-Za-z0-9_$]+\\b";
private static final String REGEX_CLASS_SLASH = "\\b(?:[A-Za-z0-9_$]+/)*[A-Za-z0-9_$]+\\b";
private static final String REGEX_LINE_NUMBER = "\\b[0-9]+\\b";
private static final String REGEX_TYPE = REGEX_CLASS + "(?:\\[\\])*";
private static final String REGEX_MEMBER = "<?\\b[A-Za-z0-9_$]+\\b>?";
private static final String REGEX_ARGUMENTS = "(?:" + REGEX_TYPE + "(?:\\s*,\\s*" + REGEX_TYPE + ")*)?";
// The class settings. 
private final String regularExpression;
private final boolean verbose;
private final File mappingFile;
private final File stackTraceFile;
private Map classMap = new HashMap();
private Map classFieldMap = new HashMap();
private Map classMethodMap = new HashMap();
}
