/** Return the line number corresponding to offset.  Uses a 1-based index. */
public int getLineOfOffset(int offset) {
    return getDefaultRootElement().getElementIndex(offset) + 1;
}
