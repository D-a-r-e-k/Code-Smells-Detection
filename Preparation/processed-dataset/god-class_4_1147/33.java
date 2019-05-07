// endValueScopeFor(IdentityConstraint)  
// a utility method for Identity constraints  
private void activateSelectorFor(IdentityConstraint ic) {
    Selector selector = ic.getSelector();
    FieldActivator activator = this;
    if (selector == null)
        return;
    XPathMatcher matcher = selector.createMatcher(activator, fElementDepth);
    fMatcherStack.addMatcher(matcher);
    matcher.startDocumentFragment();
}
