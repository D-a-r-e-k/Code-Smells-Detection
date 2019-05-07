/**
   * Get a local variable or parameter in the current stack frame.
   *
   *
   * @param xctxt The XPath context, which must be passed in order to
   * lazy evaluate variables.
   *
   * @param index Local variable index relative to the current stack
   * frame bottom.
   *
   * @return The value of the variable.
   *
   * @throws TransformerException
   */
public XObject getLocalVariable(XPathContext xctxt, int index) throws TransformerException {
    index += _currentFrameBottom;
    XObject val = _stackFrames[index];
    if (null == val)
        throw new TransformerException(XSLMessages.createXPATHMessage(XPATHErrorResources.ER_VARIABLE_ACCESSED_BEFORE_BIND, null), xctxt.getSAXLocator());
    // "Variable accessed before it is bound!", xctxt.getSAXLocator());  
    // Lazy execution of variables.  
    if (val.getType() == XObject.CLASS_UNRESOLVEDVARIABLE)
        return (_stackFrames[index] = val.execute(xctxt));
    return val;
}
