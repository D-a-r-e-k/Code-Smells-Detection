/**
	 * Sets a flag that indicates the default behavior of the navigation pane
	 * user interface element. If true, the navigation pane is visible when
	 * the content is initially activated. If false, the navigation pane is
	 * not displayed by default.
	 * @param	navigationPane	a boolean
	 */
public void setNavigationPane(PdfBoolean navigationPane) {
    put(PdfName.NAVIGATIONPANE, navigationPane);
}
