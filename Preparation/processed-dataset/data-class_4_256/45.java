/** Always throws an <code>UnsupportedOperationException</code>.
     * @param actionType ignore
     * @param action ignore
     * @throws PdfException ignore
     * @see PdfStamper#setPageAction(PdfName, PdfAction, int)
     */
@Override
public void setPageAction(PdfName actionType, PdfAction action) throws PdfException {
    throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("use.setpageaction.pdfname.actiontype.pdfaction.action.int.page"));
}
