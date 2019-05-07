/**
	 * If this dictionary refers to a child that is a file attachment added to a page,
	 * you need to specify the page with setFileAttachmentPage or setFileAttachmentPageName,
	 * and then specify the index of the attachment added to this page (or use setFileAttachmentName).
	 * @param annotation		the number of the attachment
	 */
public void setFileAttachmentIndex(int annotation) {
    put(PdfName.A, new PdfNumber(annotation));
}
