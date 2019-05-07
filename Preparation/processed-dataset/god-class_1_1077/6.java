/**
	 * Sets an array of indirect object references to file specification
	 * dictionaries, each of which describe a JavaScript file that shall
	 * be present in the Assets name tree of the RichMediaContent dictionary.
	 * @param	scripts	a PdfArray
	 */
public void setScripts(PdfArray scripts) {
    put(PdfName.SCRIPTS, scripts);
}
