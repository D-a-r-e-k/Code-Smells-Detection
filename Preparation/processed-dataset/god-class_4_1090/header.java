void method0() { 
/**
	 * Line separator property.
	 */
public static final String LINESEP = "lineSeparator";
/**
	 * Character encoding used when loading and saving.
	 * @since jEdit 3.2pre4
	 */
public static final String ENCODING = "encoding";
//}}}  
//}}}  
//{{{ Buffer events  
public static final int NORMAL_PRIORITY = 0;
public static final int HIGH_PRIORITY = 1;
//}}}  
//}}}  
//{{{ Protected members  
protected Mode mode;
protected boolean textMode;
protected UndoManager undoMgr;
//}}}  
//}}}  
//{{{ Private members  
private List<Listener> bufferListeners;
private final ReentrantReadWriteLock lock;
private ContentManager contentMgr;
private LineManager lineMgr;
private PositionManager positionMgr;
private FoldHandler foldHandler;
private IntegerArray integerArray;
private TokenMarker tokenMarker;
private boolean undoInProgress;
private boolean dirty;
private boolean readOnly;
private boolean readOnlyOverride;
private boolean transaction;
private boolean loading;
private boolean io;
private final Map<Object, PropValue> properties;
private final Object propertyLock;
}
