// cloneNode(boolean):Node  
/**
     * Retrieve information describing the abilities of this particular
     * DOM implementation. Intended to support applications that may be
     * using DOMs retrieved from several different sources, potentially
     * with different underlying representations.
     */
public DOMImplementation getImplementation() {
    // Currently implemented as a singleton, since it's hardcoded  
    // information anyway.  
    return DOMImplementationImpl.getDOMImplementation();
}
