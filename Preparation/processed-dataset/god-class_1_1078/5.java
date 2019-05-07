/**
	 * Sets a flag that indicates the default behavior of an interactive
	 * toolbar associated with this annotation. If true, a toolbar is
	 * displayed when the annotation is activated and given focus. If false,
	 * a toolbar is not displayed by default.
	 * @param	toolbar	a boolean
	 */
public void setToolbar(PdfBoolean toolbar) {
    put(PdfName.TOOLBAR, toolbar);
}
