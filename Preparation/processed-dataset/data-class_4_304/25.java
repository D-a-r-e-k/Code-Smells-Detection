// scanEndElement():int  
/**
     * Scans a character reference.
     * <p>
     * <pre>
     * [66] CharRef ::= '&#' [0-9]+ ';' | '&#x' [0-9a-fA-F]+ ';'
     * </pre>
     */
protected void scanCharReference() throws IOException, XNIException {
    fStringBuffer2.clear();
    int ch = scanCharReferenceValue(fStringBuffer2, null);
    fMarkupDepth--;
    if (ch != -1) {
        // call handler  
        if (fDocumentHandler != null) {
            if (fNotifyCharRefs) {
                fDocumentHandler.startGeneralEntity(fCharRefLiteral, null, null, null);
            }
            Augmentations augs = null;
            if (fValidation && ch <= 0x20) {
                if (fTempAugmentations != null) {
                    fTempAugmentations.removeAllItems();
                } else {
                    fTempAugmentations = new AugmentationsImpl();
                }
                augs = fTempAugmentations;
                augs.putItem(Constants.CHAR_REF_PROBABLE_WS, Boolean.TRUE);
            }
            fDocumentHandler.characters(fStringBuffer2, augs);
            if (fNotifyCharRefs) {
                fDocumentHandler.endGeneralEntity(fCharRefLiteral, null);
            }
        }
    }
}
