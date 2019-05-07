//	[M4] Adding a PdfPTable 
/** Adds a <CODE>PdfPTable</CODE> to the document.
     * @param ptable the <CODE>PdfPTable</CODE> to be added to the document.
     * @throws DocumentException on error
     */
void addPTable(PdfPTable ptable) throws DocumentException {
    ColumnText ct = new ColumnText(writer.getDirectContent());
    // if the table prefers to be on a single page, and it wouldn't 
    //fit on the current page, start a new page. 
    if (ptable.getKeepTogether() && !fitsPage(ptable, 0f) && currentHeight > 0) {
        newPage();
    }
    // add dummy paragraph if we aren't at the top of a page, so that 
    // spacingBefore will be taken into account by ColumnText 
    if (currentHeight > 0) {
        Paragraph p = new Paragraph();
        p.setLeading(0);
        ct.addElement(p);
    }
    ct.addElement(ptable);
    boolean he = ptable.isHeadersInEvent();
    ptable.setHeadersInEvent(true);
    int loop = 0;
    while (true) {
        ct.setSimpleColumn(indentLeft(), indentBottom(), indentRight(), indentTop() - currentHeight);
        int status = ct.go();
        if ((status & ColumnText.NO_MORE_TEXT) != 0) {
            text.moveText(0, ct.getYLine() - indentTop() + currentHeight);
            currentHeight = indentTop() - ct.getYLine();
            break;
        }
        if (indentTop() - currentHeight == ct.getYLine())
            ++loop;
        else
            loop = 0;
        if (loop == 3) {
            add(new Paragraph("ERROR: Infinite table loop"));
            break;
        }
        newPage();
    }
    ptable.setHeadersInEvent(he);
}
