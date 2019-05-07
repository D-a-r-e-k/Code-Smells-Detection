/**
	 * If this dictionary refers to a child that is a file attachment added to a page,
	 * you need to specify the page with setFileAttachmentPage or setFileAttachmentPageName,
	 * and then specify the name of the attachment added to this page (or use setFileAttachmentIndex).
	 * @param name		the name of the attachment
	 */
public void setFileAttachmentName(String name) {
    put(PdfName.A, new PdfString(name, PdfObject.TEXT_UNICODE));
}
