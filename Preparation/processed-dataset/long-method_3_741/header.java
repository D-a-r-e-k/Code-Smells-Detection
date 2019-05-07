void method0() { 
/*

    data store:
    keys: {array of primitive | array of object}
    values: {none | array of primitive | array of object} same size as keys
    objects support : hashCode(), equals()

    implemented types of keyTable:
    {objectKeyTable: variable size Object[] array for keys |
    intKeyTable: variable size int[] for keys |
    longKeyTable: variable size long[] for keys }

    implemented types of valueTable:
    {objectValueTable: variable size Object[] array for values |
    intValueTable: variable size int[] for values |
    longValueTable: variable size long[] for values}

    valueTable does not exist for sets or for object pools

    hash index:
    hashTable: fixed size int[] array for hash lookup into keyTable
    linkTable: pointer to the next key ; size equal or larger than hashTable
    but equal to the valueTable

    access count table:
    {none |
    variable size int[] array for access count} same size as xxxKeyTable
*/
// 
boolean isIntKey;
boolean isLongKey;
boolean isObjectKey;
boolean isNoValue;
boolean isIntValue;
boolean isLongValue;
boolean isObjectValue;
protected boolean isTwoObjectValue;
protected boolean isList;
// 
private ValuesIterator valuesIterator;
// 
protected HashIndex hashIndex;
// 
protected int[] intKeyTable;
protected Object[] objectKeyTable;
protected long[] longKeyTable;
// 
protected int[] intValueTable;
protected Object[] objectValueTable;
protected long[] longValueTable;
// 
protected int accessMin;
protected int accessCount;
protected int[] accessTable;
protected boolean[] multiValueTable;
protected Object[] objectValueTable2;
// 
final float loadFactor;
final int initialCapacity;
int threshold;
protected int maxCapacity;
protected int purgePolicy = NO_PURGE;
protected boolean minimizeOnEmpty;
// 
boolean hasZeroKey;
int zeroKeyIndex = -1;
// keyOrValueTypes 
protected static final int noKeyOrValue = 0;
protected static final int intKeyOrValue = 1;
protected static final int longKeyOrValue = 2;
protected static final int objectKeyOrValue = 3;
// purgePolicy 
protected static final int NO_PURGE = 0;
protected static final int PURGE_ALL = 1;
protected static final int PURGE_HALF = 2;
protected static final int PURGE_QUARTER = 3;
// 
public static final int ACCESS_MAX = Integer.MAX_VALUE - (1 << 20);
}
