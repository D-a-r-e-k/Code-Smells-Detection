/**
   * Get the element at the given index, regardless of stackframe.
   *
   * @param i index from zero.
   *
   * @return The item at the given index.
   */
public XObject elementAt(final int i) {
    return _stackFrames[i];
}
