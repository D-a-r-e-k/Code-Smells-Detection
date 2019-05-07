/**
	 * Sets this buffer's edit mode. Note that calling this before a buffer
	 * is loaded will have no effect; in that case, set the "mode" property
	 * to the name of the mode. A bit inelegant, I know...
	 * @param mode The mode
	 */
public void setMode(Mode mode) {
    /* This protects against stupid people (like me)
		 * doing stuff like buffer.setMode(jEdit.getMode(...)); */
    if (mode == null)
        throw new NullPointerException("Mode must be non-null");
    this.mode = mode;
    textMode = "text".equals(mode.getName());
    setTokenMarker(mode.getTokenMarker());
    resetCachedProperties();
    propertiesChanged();
}
