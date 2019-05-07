/**
     * Always throws an <code>UnsupportedOperationException</code>.
     * @param transition ignore
     */
@Override
public void setTransition(PdfTransition transition) {
    throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("use.setpageaction.pdfname.actiontype.pdfaction.action.int.page"));
}
