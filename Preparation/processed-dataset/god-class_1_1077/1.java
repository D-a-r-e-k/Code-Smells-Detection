/**
	 * Sets the activation condition.
	 * Set it to XA if the annotation is explicitly activated by a user action
	 * or script (this is the default).
	 * To PO, if the annotation is activated as soon as the page that contains
	 * the annotation receives focus as the current page.
	 * To PV, if the annotation is activated as soon as any part of the page
	 * that contains the annotation becomes visible. One example is in a
	 * multiple-page presentation. Only one page is the current page although
	 * several are visible.
	 * @param	condition	possible values are:
	 * 		PdfName.XA, PdfName.PO, or PdfName.PV
	 */
public void setCondition(PdfName condition) {
    put(PdfName.CONDITION, condition);
}
