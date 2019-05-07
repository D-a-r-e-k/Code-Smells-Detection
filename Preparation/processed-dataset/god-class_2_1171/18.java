/**
   * Set a global variable or parameter in the global stack frame.
   *
   *
   * @param index Local variable index relative to the global stack frame
   * bottom.
   *
   * @param val The value of the variable that is being set.
   */
public void setGlobalVariable(final int index, final XObject val) {
    _stackFrames[index] = val;
}
