// startValueScopeFor(IdentityConstraint identityConstraint)  
/**
     * Request to activate the specified field. This method returns the
     * matcher for the field.
     *
     * @param field The field to activate.
     */
public XPathMatcher activateField(Field field, int initialDepth) {
    ValueStore valueStore = fValueStoreCache.getValueStoreFor(field.getIdentityConstraint(), initialDepth);
    XPathMatcher matcher = field.createMatcher(valueStore);
    fMatcherStack.addMatcher(matcher);
    matcher.startDocumentFragment();
    return matcher;
}
