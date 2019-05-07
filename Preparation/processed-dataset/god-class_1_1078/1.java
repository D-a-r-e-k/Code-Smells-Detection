/**
	 * Sets the style for the presentation;
	 * can be embedded or windowed.
	 * @param	style PdfName.EMBEDDED or PdfName.WINDOWED
	 */
public void setStyle(PdfName style) {
    put(PdfName.STYLE, style);
}
