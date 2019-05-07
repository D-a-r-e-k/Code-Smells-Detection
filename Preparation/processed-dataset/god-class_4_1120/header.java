void method0() { 
public static final int LEFT = 0;
public static final int RIGHT = 1;
public static final int UNARY = 1;
public static final int BINARY = 2;
// 
// 
static final Expression[] emptyArray = new Expression[] {};
// 
static final Expression EXPR_TRUE = new ExpressionLogical(true);
static final Expression EXPR_FALSE = new ExpressionLogical(false);
// 
static final OrderedIntHashSet aggregateFunctionSet = new OrderedIntHashSet();
static final OrderedIntHashSet columnExpressionSet = new OrderedIntHashSet();
static final OrderedIntHashSet subqueryExpressionSet = new OrderedIntHashSet();
static final OrderedIntHashSet subqueryAggregateExpressionSet = new OrderedIntHashSet();
static final OrderedIntHashSet functionExpressionSet = new OrderedIntHashSet();
static final OrderedIntHashSet emptyExpressionSet = new OrderedIntHashSet();
// type 
protected int opType;
// type qualifier 
protected int exprSubType;
// 
SimpleName alias;
// aggregate 
private boolean isAggregate;
// VALUE 
protected Object valueData;
protected Expression[] nodes;
Type[] nodeDataTypes;
// QUERY - in single value selects, IN, EXISTS etc. 
SubQuery subQuery;
// for query and value lists, etc 
boolean isCorrelated;
// for COLUMN 
int columnIndex = -1;
// data type 
protected Type dataType;
// 
int queryTableColumnIndex = -1;
// >= 0 when it is used for order by 
// index of a session-dependent field 
int parameterIndex = -1;
// 
int rangePosition = -1;
// 
boolean isColumnEqual;
}
