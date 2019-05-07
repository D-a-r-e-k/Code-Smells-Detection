//}}}  
//{{{ resetCachedProperties() method  
public void resetCachedProperties() {
    // Need to reset properties that were cached defaults,  
    // since the defaults might have changed.  
    Iterator<PropValue> iter = properties.values().iterator();
    while (iter.hasNext()) {
        PropValue value = iter.next();
        if (value.defaultValue)
            iter.remove();
    }
}
