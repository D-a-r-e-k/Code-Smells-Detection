// endElement(QName)  
/** 
     * The start of a CDATA section. 
     * @param augs   Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void startCDATA(Augmentations augs) throws XNIException {
    if (fPerformValidation && fInElementContent) {
        charDataInContent();
    }
    fInCDATASection = true;
    // call handlers  
    if (fDocumentHandler != null) {
        fDocumentHandler.startCDATA(augs);
    }
}
