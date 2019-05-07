//}}}  
//}}}  
//{{{ Property methods  
//{{{ propertiesChanged() method  
/**
	 * Reloads settings from the properties. This should be called
	 * after the <code>syntax</code> or <code>folding</code>
	 * buffer-local properties are changed.
	 */
public void propertiesChanged() {
    String folding = getStringProperty("folding");
    FoldHandler handler = FoldHandler.getFoldHandler(folding);
    if (handler != null) {
        setFoldHandler(handler);
    } else {
        if (folding != null)
            Log.log(Log.WARNING, this, "invalid 'folding' property: " + folding);
        setFoldHandler(new DummyFoldHandler());
    }
}
