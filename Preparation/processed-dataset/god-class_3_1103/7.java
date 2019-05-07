/** Set the ends style.  This should be one of the following values:
      OPEN_ENDS, CLOSED_ENDS, or FLAT_ENDS. */
public void setEndsStyle(int style) {
    endsStyle = style;
    closed = (style == CLOSED_ENDS);
    clearCachedMesh();
}
