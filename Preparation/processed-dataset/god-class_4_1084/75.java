/**
     * Checks if a <CODE>PdfPTable</CODE> fits the current page of the <CODE>PdfDocument</CODE>.
     *
     * @param	table	the table that has to be checked
     * @param	margin	a certain margin
     * @return	<CODE>true</CODE> if the <CODE>PdfPTable</CODE> fits the page, <CODE>false</CODE> otherwise.
     */
boolean fitsPage(PdfPTable table, float margin) {
    if (!table.isLockedWidth()) {
        float totalWidth = (indentRight() - indentLeft()) * table.getWidthPercentage() / 100;
        table.setTotalWidth(totalWidth);
    }
    // ensuring that a new line has been started. 
    ensureNewLine();
    return table.getTotalHeight() + (currentHeight > 0 ? table.spacingBefore() : 0f) <= indentTop() - currentHeight - indentBottom() - margin;
}
