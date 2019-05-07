void method0() { 
public static final int RESULT_METADATA = 1;
public static final int SIMPLE_RESULT_METADATA = 2;
public static final int UPDATE_RESULT_METADATA = 3;
public static final int PARAM_METADATA = 4;
public static final int GENERATED_INDEX_METADATA = 5;
public static final int GENERATED_NAME_METADATA = 6;
// 
private int type;
// values overriding table column 
public String[] columnLabels;
public Type[] columnTypes;
private int columnCount;
private int extendedColumnCount;
public static final ResultMetaData emptyResultMetaData = newResultMetaData(0);
public static final ResultMetaData emptyParamMetaData = newParameterMetaData(0);
// column indexes for mapping or for generated columns 
public int[] colIndexes;
// columns for data columns 
public ColumnBase[] columns;
// param mode and nullability for parameter metadata 
public byte[] paramModes;
public byte[] paramNullable;
}
