/**
	 * Identifies the document that will be initially presented
	 * in the user interface.
	 * @param description	the description that was used when attaching the file to the document
	 */
public void setInitialDocument(String description) {
    put(PdfName.D, new PdfString(description, null));
}
