/**
     * Stop this component and implement the requirements
     * of {@link LifecycleBase#stopInternal()}.
     *
     * @exception LifecycleException if this component detects a fatal error
     *  that prevents this component from being used
     */
@Override
protected synchronized void stopInternal() throws LifecycleException {
    setState(LifecycleState.STOPPING);
    close();
}
