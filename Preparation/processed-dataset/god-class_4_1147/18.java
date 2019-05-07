// ignorableWhitespace(XMLString)  
/**
     * The end of an element.
     *
     * @param element The name of the element.
     * @param augs     Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void endElement(QName element, Augmentations augs) throws XNIException {
    // in the case where there is a {value constraint}, and the element  
    // doesn't have any text content, add a characters call.  
    fDefaultValue = null;
    Augmentations modifiedAugs = handleEndElement(element, augs);
    // call handlers  
    if (fDocumentHandler != null) {
        if (!fSchemaElementDefault || fDefaultValue == null) {
            fDocumentHandler.endElement(element, modifiedAugs);
        } else {
            fDocumentHandler.characters(fDefaultValue, null);
            fDocumentHandler.endElement(element, modifiedAugs);
        }
    }
}
