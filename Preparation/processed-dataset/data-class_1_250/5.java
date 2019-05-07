/**
	 * Sets a RichMediaPresentation dictionary that contains information
	 * as to how the annotation and user interface elements will be visually
	 * laid out and drawn.
	 * @param	richMediaPresentation	a RichMediaPresentation object
	 */
public void setPresentation(RichMediaPresentation richMediaPresentation) {
    put(PdfName.PRESENTATION, richMediaPresentation);
}
