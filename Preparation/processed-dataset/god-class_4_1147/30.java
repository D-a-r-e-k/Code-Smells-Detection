// reset(XMLComponentManager)  
//  
// FieldActivator methods  
//  
/**
     * Start the value scope for the specified identity constraint. This
     * method is called when the selector matches in order to initialize
     * the value store.
     *
     * @param identityConstraint The identity constraint.
     */
public void startValueScopeFor(IdentityConstraint identityConstraint, int initialDepth) {
    ValueStoreBase valueStore = fValueStoreCache.getValueStoreFor(identityConstraint, initialDepth);
    valueStore.startValueScope();
}
