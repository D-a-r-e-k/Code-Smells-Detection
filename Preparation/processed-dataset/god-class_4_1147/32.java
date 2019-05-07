// activateField(Field):XPathMatcher  
/**
     * Ends the value scope for the specified identity constraint.
     *
     * @param identityConstraint The identity constraint.
     */
public void endValueScopeFor(IdentityConstraint identityConstraint, int initialDepth) {
    ValueStoreBase valueStore = fValueStoreCache.getValueStoreFor(identityConstraint, initialDepth);
    valueStore.endValueScope();
}
