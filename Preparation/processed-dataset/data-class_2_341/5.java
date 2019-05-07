/**
   * Reset the stack to a start position.
   * @param linksSize Initial stack size to use
   * @param varArraySize Initial variable array size to use
   */
protected void reset(int linksSize, int varArraySize) {
    _frameTop = 0;
    _linksTop = 0;
    // Don't bother reallocating _links array if it exists already  
    if (_links == null) {
        _links = new int[linksSize];
    }
    // Adding one here to the stack of frame positions will allow us always   
    // to look one under without having to check if we're at zero.  
    // (As long as the caller doesn't screw up link/unlink.)  
    _links[_linksTop++] = 0;
    // Get a clean _stackFrames array and discard the old one.  
    _stackFrames = new XObject[varArraySize];
}
