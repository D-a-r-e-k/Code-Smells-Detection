//}}}  
//{{{ toggleWordWrap() method  
/**
	 * Toggles word wrap between the three available modes. This is used
	 * by the status bar.
	 * @param view We show a message in the view's status bar
	 * @since jEdit 4.1pre3
	 */
public void toggleWordWrap(View view) {
    String wrap = getStringProperty("wrap");
    if (wrap.equals("none"))
        wrap = "soft";
    else if (wrap.equals("soft"))
        wrap = "hard";
    else if (wrap.equals("hard"))
        wrap = "none";
    view.getStatus().setMessageAndClear(jEdit.getProperty("view.status.wrap-changed", new String[] { wrap }));
    setProperty("wrap", wrap);
    propertiesChanged();
}
