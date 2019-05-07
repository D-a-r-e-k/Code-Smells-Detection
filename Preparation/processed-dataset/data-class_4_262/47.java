//}}}  
//{{{ getMarkerStatusPrompt() method  
/**
	 * Returns the status prompt for the given marker action. Only
	 * intended to be called from <code>actions.xml</code>.
	 * @since jEdit 4.2pre2
	 */
public String getMarkerStatusPrompt(String action) {
    return jEdit.getProperty("view.status." + action, new String[] { getMarkerNameString() });
}
