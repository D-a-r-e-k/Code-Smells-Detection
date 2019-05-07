void method0() { 
public static final byte ACTION_NONE = 0;
public static final byte ACTION_INSERT = 1;
public static final byte ACTION_DELETE = 2;
public static final byte ACTION_DELETE_FINAL = 3;
public static final byte ACTION_INSERT_DELETE = 4;
public static final byte ACTION_REF = 5;
public static final byte ACTION_CHECK = 6;
public static final byte ACTION_DEBUG = 7;
// 
RowActionBase next;
Session session;
long actionTimestamp;
long commitTimestamp;
byte type;
boolean deleteComplete;
boolean rolledback;
boolean prepared;
int[] changeColumnMap;
}
