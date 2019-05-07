//}}}  
//{{{ toggleLineSeparator() method  
/**
	 * Toggles the line separator between the three available settings.
	 * This is used by the status bar.
	 * @param view We show a message in the view's status bar
	 * @since jEdit 4.1pre3
	 */
public void toggleLineSeparator(View view) {
    String status = null;
    String lineSep = getStringProperty(LINESEP);
    if ("\n".equals(lineSep)) {
        status = "windows";
        lineSep = "\r\n";
    } else if ("\r\n".equals(lineSep)) {
        status = "mac";
        lineSep = "\r";
    } else if ("\r".equals(lineSep)) {
        status = "unix";
        lineSep = "\n";
    }
    view.getStatus().setMessageAndClear(jEdit.getProperty("view.status.linesep-changed", new String[] { jEdit.getProperty("lineSep." + status) }));
    setProperty(LINESEP, lineSep);
    setDirty(true);
    propertiesChanged();
}
