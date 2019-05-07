// setScannerState(int)  
/**
     * Sets the dispatcher.
     *
     * @param dispatcher The new dispatcher.
     */
protected final void setDispatcher(Dispatcher dispatcher) {
    fDispatcher = dispatcher;
    if (DEBUG_DISPATCHER) {
        System.out.print("%%% setDispatcher: ");
        System.out.print(getDispatcherName(dispatcher));
        System.out.println();
    }
}
