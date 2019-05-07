/**
	 * Change the current page size
	 *
	 * @param pgsize
	 *            page size (in points)
	 */
public void setPageSize(Dimension pgsize) {
    DocumentAtom docatom = _documentRecord.getDocumentAtom();
    docatom.setSlideSizeX(pgsize.width * Shape.MASTER_DPI / Shape.POINT_DPI);
    docatom.setSlideSizeY(pgsize.height * Shape.MASTER_DPI / Shape.POINT_DPI);
}
