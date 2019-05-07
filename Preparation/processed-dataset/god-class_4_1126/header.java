void method0() { 
// fields 
private final long persistenceId;
protected final HsqlName name;
private final boolean[] colCheck;
final int[] colIndex;
private final int[] defaultColMap;
final Type[] colTypes;
private final boolean[] colDesc;
private final boolean[] nullsLast;
final boolean isSimpleOrder;
final boolean isSimple;
protected final boolean isPK;
// PK with or without columns 
protected final boolean isUnique;
// DDL uniqueness 
protected final boolean isConstraint;
private final boolean isForward;
private int depth;
private static final IndexRowIterator emptyIterator = new IndexRowIterator(null, (PersistentStore) null, null, null, false, false);
protected TableBase table;
int position;
// 
Object[] nullData;
// 
ReadWriteLock lock = new ReentrantReadWriteLock();
Lock readLock = lock.readLock();
Lock writeLock = lock.writeLock();
}
