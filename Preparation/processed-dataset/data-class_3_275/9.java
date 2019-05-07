/** Make sure the ends style is consistent with the closed flag.  Generally,
      setEndsStyle() should be used instead of this method. */
public void setClosed(boolean isClosed) {
    super.setClosed(isClosed);
    if (isClosed)
        endsStyle = CLOSED_ENDS;
    else if (endsStyle == CLOSED_ENDS)
        endsStyle = OPEN_ENDS;
}
