void method0() { 
private static final int[] defaultLimits = new int[] { 0, Integer.MAX_VALUE, Integer.MAX_VALUE };
// 
public int resultRangePosition;
public boolean isDistinctSelect;
public boolean isAggregated;
public boolean isGrouped;
RangeVariable[] rangeVariables;
private HsqlArrayList rangeVariableList;
Expression queryCondition;
Expression checkQueryCondition;
private Expression havingCondition;
Expression rowExpression;
Expression[] exprColumns;
private HsqlArrayList exprColumnList;
public int indexLimitVisible;
private int indexLimitRowId;
private int groupByColumnCount;
// columns in 'group by' 
private int havingColumnCount;
// columns in 'having' (0 or 1) 
private int indexStartHaving;
public int indexStartOrderBy;
public int indexStartAggregates;
private int indexLimitExpressions;
public int indexLimitData;
private boolean hasRowID;
private boolean isSimpleCount;
private boolean hasMemoryRow;
// 
public boolean isUniqueResultRows;
private boolean simpleLimit = true;
// true if maxrows can be uses as is 
// 
Type[] columnTypes;
private ArrayListIdentity aggregateSet;
// 
private ArrayListIdentity resolvedSubqueryExpressions = null;
// 
// 
private boolean[] aggregateCheck;
// 
private OrderedHashSet tempSet = new OrderedHashSet();
// 
int[] columnMap;
private Table baseTable;
private OrderedHashSet conditionTables;
// for view super-view references 
// 
public Index groupIndex;
}
