/**
	 * Gets a PDF Name from an array or returns the object that was passed.
	 */
private PdfObject simplifyColorspace(PdfArray obj) {
    if (obj == null)
        return obj;
    PdfName first = obj.getAsName(0);
    if (PdfName.CALGRAY.equals(first))
        return PdfName.DEVICEGRAY;
    else if (PdfName.CALRGB.equals(first))
        return PdfName.DEVICERGB;
    else
        return obj;
}
