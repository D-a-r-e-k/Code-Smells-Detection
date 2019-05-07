//}}}  
//}}}  
//{{{ Edit modes, syntax highlighting  
//{{{ setMode() method  
/**
	 * Sets this buffer's edit mode by calling the accept() method
	 * of each registered edit mode.
	 */
public void setMode() {
    String userMode = getStringProperty("mode");
    if (userMode != null) {
        unsetProperty("mode");
        Mode m = ModeProvider.instance.getMode(userMode);
        if (m != null) {
            setMode(m);
            return;
        }
    }
    String firstLine = getLineText(0);
    Mode mode = ModeProvider.instance.getModeForFile(name, firstLine);
    if (mode != null) {
        setMode(mode);
        return;
    }
    Mode defaultMode = jEdit.getMode(jEdit.getProperty("buffer.defaultMode"));
    if (defaultMode == null)
        defaultMode = jEdit.getMode("text");
    if (defaultMode != null)
        setMode(defaultMode);
}
