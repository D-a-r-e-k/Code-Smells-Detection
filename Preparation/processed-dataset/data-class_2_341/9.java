/**
   * Free up the stack frame that was last allocated with
   * {@link #link(int size)}.
   */
public void unlink() {
    _frameTop = _links[--_linksTop];
    _currentFrameBottom = _links[_linksTop - 1];
}
