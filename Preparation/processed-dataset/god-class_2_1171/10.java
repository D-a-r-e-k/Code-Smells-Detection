/**
   * Free up the stack frame that was last allocated with
   * {@link #link(int size)}.
   * @param currentFrame The current frame to set to 
   * after the unlink.
   */
public void unlink(int currentFrame) {
    _frameTop = _links[--_linksTop];
    _currentFrameBottom = currentFrame;
}
