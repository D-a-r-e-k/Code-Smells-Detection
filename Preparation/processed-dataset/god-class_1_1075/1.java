/**
	 * If this dictionary refers to a child that is a document level attachment,
	 * you need to specify the name that was used to attach the document.
	 * @param	target	the name in the EmbeddedFiles name tree
	 */
public void setEmbeddedFileName(String target) {
    put(PdfName.N, new PdfString(target, null));
}
