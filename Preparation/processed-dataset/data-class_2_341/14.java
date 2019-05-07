/**
   * Get a local variable or parameter in the current stack frame.
   *
   *
   * @param index Local variable index relative to the given
   * frame bottom.
   * NEEDSDOC @param frame
   *
   * @return The value of the variable.
   *
   * @throws TransformerException
   */
public XObject getLocalVariable(int index, int frame) throws TransformerException {
    index += frame;
    XObject val = _stackFrames[index];
    return val;
}
