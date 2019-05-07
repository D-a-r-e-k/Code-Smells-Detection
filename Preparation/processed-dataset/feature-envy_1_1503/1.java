/**
	 * Constructs a PDF Collection.
	 * @param	type	the type of PDF collection.
	 */
public PdfCollection(int type) {
    super(PdfName.COLLECTION);
    switch(type) {
        case TILE:
            put(PdfName.VIEW, PdfName.T);
            break;
        case HIDDEN:
            put(PdfName.VIEW, PdfName.H);
            break;
        case CUSTOM:
            put(PdfName.VIEW, PdfName.C);
            break;
        default:
            put(PdfName.VIEW, PdfName.D);
    }
}
