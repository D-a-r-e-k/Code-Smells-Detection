// scanEntityReference()  
// utility methods  
/** 
     * Calls document handler with a single character resulting from
     * built-in entity resolution. 
     *
     * @param c
     * @param entity built-in name
     */
private void handleCharacter(char c, String entity) throws XNIException {
    if (fDocumentHandler != null) {
        if (fNotifyBuiltInRefs) {
            fDocumentHandler.startGeneralEntity(entity, null, null, null);
        }
        fSingleChar[0] = c;
        fTempString.setValues(fSingleChar, 0, 1);
        fDocumentHandler.characters(fTempString, null);
        if (fNotifyBuiltInRefs) {
            fDocumentHandler.endGeneralEntity(entity, null);
        }
    }
}
