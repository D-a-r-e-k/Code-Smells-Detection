/**
   * Set a local variable or parameter in the current stack frame.
   *
   *
   * @param index Local variable index relative to the current stack
   * frame bottom.
   *
   * @param val The value of the variable that is being set.
   */
public void setLocalVariable(int index, XObject val) {
    _stackFrames[index + _currentFrameBottom] = val;
}
