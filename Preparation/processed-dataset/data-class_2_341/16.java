/**
   * Tell if a local variable has been set or not.
   *
   * @param index Local variable index relative to the current stack
   * frame bottom.
   *
   * @return true if the value at the index is not null.
   *
   * @throws TransformerException
   */
public boolean isLocalSet(int index) throws TransformerException {
    return (_stackFrames[index + _currentFrameBottom] != null);
}
