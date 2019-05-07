/**
   * Get the position from where the search should start,
   * which is either the searchStart property, or the top
   * of the stack if that value is -1.
   *
   * @return The current stack frame position.
   */
public int getStackFrame() {
    return _currentFrameBottom;
}
