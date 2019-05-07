/** Additional-actions defining the actions to be taken in
     * response to various trigger events affecting the document
     * as a whole. The actions types allowed are: <CODE>DOCUMENT_CLOSE</CODE>,
     * <CODE>WILL_SAVE</CODE>, <CODE>DID_SAVE</CODE>, <CODE>WILL_PRINT</CODE>
     * and <CODE>DID_PRINT</CODE>.
     *
     * @param actionType the action type
     * @param action the action to execute in response to the trigger
     * @throws PdfException on invalid action type
     */
@Override
public void setAdditionalAction(PdfName actionType, PdfAction action) throws PdfException {
    if (!(actionType.equals(DOCUMENT_CLOSE) || actionType.equals(WILL_SAVE) || actionType.equals(DID_SAVE) || actionType.equals(WILL_PRINT) || actionType.equals(DID_PRINT))) {
        throw new PdfException(MessageLocalization.getComposedMessage("invalid.additional.action.type.1", actionType.toString()));
    }
    PdfDictionary aa = reader.getCatalog().getAsDict(PdfName.AA);
    if (aa == null) {
        if (action == null)
            return;
        aa = new PdfDictionary();
        reader.getCatalog().put(PdfName.AA, aa);
    }
    markUsed(aa);
    if (action == null)
        aa.remove(actionType);
    else
        aa.put(actionType, action);
}
