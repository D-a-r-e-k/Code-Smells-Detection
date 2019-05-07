/** This is called when this method is GC'd.  Since this class implements edu.rice.cs.drjava.model.Finalizable, it
    * must notify its listeners
    */
protected void finalize() {
    FinalizationEvent<DefinitionsDocument> fe = new FinalizationEvent<DefinitionsDocument>(this);
    synchronized (_finalizationListeners) {
        for (FinalizationListener<DefinitionsDocument> fl : _finalizationListeners) {
            fl.finalized(fe);
        }
    }
}
