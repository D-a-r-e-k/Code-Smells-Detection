/**
   * Get a global variable or parameter from the global stack frame.
   *
   *
   * @param xctxt The XPath context, which must be passed in order to
   * lazy evaluate variables.
   *
   * @param index Global variable index relative to the global stack
   * frame bottom.
   *
   * @return The value of the variable.
   *
   * @throws TransformerException
   */
public XObject getGlobalVariable(XPathContext xctxt, final int index, boolean destructiveOK) throws TransformerException {
    XObject val = _stackFrames[index];
    // Lazy execution of variables.  
    if (val.getType() == XObject.CLASS_UNRESOLVEDVARIABLE)
        return (_stackFrames[index] = val.execute(xctxt));
    return destructiveOK ? val : val.getFresh();
}
