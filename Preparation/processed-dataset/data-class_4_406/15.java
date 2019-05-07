/**
	 * Return the current page size
	 */
public Dimension getPageSize() {
    DocumentAtom docatom = _documentRecord.getDocumentAtom();
    int pgx = (int) docatom.getSlideSizeX() * Shape.POINT_DPI / Shape.MASTER_DPI;
    int pgy = (int) docatom.getSlideSizeY() * Shape.POINT_DPI / Shape.MASTER_DPI;
    return new Dimension(pgx, pgy);
}
