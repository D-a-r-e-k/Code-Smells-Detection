/**
     * Always throws an <code>UnsupportedOperationException</code>.
     * @param seconds ignore
     */
@Override
public void setDuration(int seconds) {
    throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("use.setpageaction.pdfname.actiontype.pdfaction.action.int.page"));
}
