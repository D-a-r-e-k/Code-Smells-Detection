/**
   * Use this to clear the variables in a section of the stack.  This is
   * used to clear the parameter section of the stack, so that default param
   * values can tell if they've already been set.  It is important to note that
   * this function has a 1K limitation.
   *
   * @param start The start position, relative to the current local stack frame.
   * @param len The number of slots to be cleared.
   */
public void clearLocalSlots(int start, int len) {
    start += _currentFrameBottom;
    System.arraycopy(m_nulls, 0, _stackFrames, start, len);
}
