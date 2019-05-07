// begin debug code 
/** Closes this DefinitionsDocument (but not the enclosing OpenDefinitionsDocument).  Called when this is kicked out
    * of the document cache so that this can be GC'd. */
public void close() {
    _removeIndenter();
    synchronized (_closedListeners) {
        for (DocumentClosedListener l : _closedListeners) {
            l.close();
        }
        _closedListeners.clear();
    }
}
