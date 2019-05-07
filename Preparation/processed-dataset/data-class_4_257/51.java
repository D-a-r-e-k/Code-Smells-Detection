void addAdditionalAction(PdfName actionType, PdfAction action) {
    if (additionalActions == null) {
        additionalActions = new PdfDictionary();
    }
    if (action == null)
        additionalActions.remove(actionType);
    else
        additionalActions.put(actionType, action);
    if (additionalActions.size() == 0)
        additionalActions = null;
}
