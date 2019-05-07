// characters(XMLString)  
/**
     * Ignorable whitespace. For this method to be called, the document
     * source must have some way of determining that the text containing
     * only whitespace characters should be considered ignorable. For
     * example, the validator can determine if a length of whitespace
     * characters in the document are ignorable based on the element
     * content model.
     *
     * @param text The ignorable whitespace.
     * @param augs     Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void ignorableWhitespace(XMLString text, Augmentations augs) throws XNIException {
    handleIgnorableWhitespace(text);
    // call handlers  
    if (fDocumentHandler != null) {
        fDocumentHandler.ignorableWhitespace(text, augs);
    }
}
