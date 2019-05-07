/** Registers a finalization listener with the specific instance of the ddoc
    * <p><b>NOTE:</b><i>This should only be used by test cases.  This is to ensure that
    * we don't spring memory leaks by allowing our unit tests to keep track of 
    * whether objects are being finalized (garbage collected)</i></p>
    * @param fl the listener to register
    */
public void addFinalizationListener(FinalizationListener<DefinitionsDocument> fl) {
    synchronized (_finalizationListeners) {
        _finalizationListeners.add(fl);
    }
}
