/**
   * Reset the stack to a start position.
   */
public void reset() {
    // If the stack was previously allocated, assume that about the same  
    // amount of stack space will be needed again; otherwise, use a very  
    // large stack size.  
    int linksSize = (_links == null) ? XPathContext.RECURSIONLIMIT : _links.length;
    int varArraySize = (_stackFrames == null) ? XPathContext.RECURSIONLIMIT * 2 : _stackFrames.length;
    reset(linksSize, varArraySize);
}
