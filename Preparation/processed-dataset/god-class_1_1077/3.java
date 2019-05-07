/**
	 * Sets an indirect object reference to a 3D view dictionary
	 * that shall also be referenced by the Views array within the
	 * annotation's RichMediaContent dictionary.
	 * @param	view	an indirect reference
	 */
public void setView(PdfIndirectReference view) {
    put(PdfName.VIEW, view);
}
