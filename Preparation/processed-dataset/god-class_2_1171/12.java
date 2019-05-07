/**
   * Set a local variable or parameter in the specified stack frame.
   *
   *
   * @param index Local variable index relative to the current stack
   * frame bottom.
   * NEEDSDOC @param stackFrame
   *
   * @param val The value of the variable that is being set.
   */
public void setLocalVariable(int index, XObject val, int stackFrame) {
    _stackFrames[index + stackFrame] = val;
}
