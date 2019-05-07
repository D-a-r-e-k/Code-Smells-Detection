/**
     * Start this component and implement the requirements
     * of {@link LifecycleBase#startInternal()}.
     *
     * @exception LifecycleException if this component detects a fatal error
     *  that prevents this component from being used
     */
@Override
protected synchronized void startInternal() throws LifecycleException {
    try {
        open();
    } catch (SQLException e) {
        throw new LifecycleException(e);
    }
    setState(LifecycleState.STARTING);
}
