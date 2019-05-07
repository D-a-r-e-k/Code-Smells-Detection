//}}}  
//{{{ setMode() methods  
/**
	 * Sets this buffer's edit mode. Note that calling this before a buffer
	 * is loaded will have no effect; in that case, set the "mode" property
	 * to the name of the mode. A bit inelegant, I know...
	 * @param mode The mode name
	 * @since jEdit 4.2pre1
	 */
public void setMode(String mode) {
    setMode(ModeProvider.instance.getMode(mode));
}
