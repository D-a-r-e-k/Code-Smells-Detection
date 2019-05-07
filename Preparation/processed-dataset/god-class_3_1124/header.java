void method0() { 
public static final int NO_SQL = 1;
public static final int CONTAINS_SQL = 2;
public static final int READS_SQL = 3;
public static final int MODIFIES_SQL = 4;
// 
public static final int LANGUAGE_JAVA = 1;
public static final int LANGUAGE_SQL = 2;
// 
public static final int PARAM_STYLE_JAVA = 1;
public static final int PARAM_STYLE_SQL = 2;
// 
static final Routine[] emptyArray = new Routine[] {};
// 
RoutineSchema routineSchema;
private HsqlName name;
private HsqlName specificName;
Type[] parameterTypes;
int typeGroups;
Type returnType;
Type[] tableType;
Table returnTable;
final int routineType;
int language = LANGUAGE_SQL;
int dataImpact = CONTAINS_SQL;
int parameterStyle;
boolean isDeterministic;
boolean isNullInputOutput;
boolean isNewSavepointLevel = true;
boolean isPSM;
boolean returnsTable;
Statement statement;
// 
boolean isAggregate;
// 
private String methodName;
Method javaMethod;
boolean javaMethodWithConnection;
private boolean isLibraryRoutine;
// 
HashMappedList parameterList = new HashMappedList();
int scopeVariableCount;
RangeVariable[] ranges;
// 
int variableCount;
// 
OrderedHashSet references;
// 
Table triggerTable;
int triggerType;
int triggerOperation;
}
