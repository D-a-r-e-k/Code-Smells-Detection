// startElement(QName,XMLAttributes, Augmentations)  
/**
     * An empty element.
     *
     * @param element    The name of the element.
     * @param attributes The element attributes.
     * @param augs     Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
    Augmentations modifiedAugs = handleStartElement(element, attributes, augs);
    // in the case where there is a {value constraint}, and the element  
    // doesn't have any text content, change emptyElement call to  
    // start + characters + end  
    fDefaultValue = null;
    // fElementDepth == -2 indicates that the schema validator was removed  
    // from the pipeline. then we don't need to call handleEndElement.  
    if (fElementDepth != -2)
        modifiedAugs = handleEndElement(element, modifiedAugs);
    // call handlers  
    if (fDocumentHandler != null) {
        if (!fSchemaElementDefault || fDefaultValue == null) {
            fDocumentHandler.emptyElement(element, attributes, modifiedAugs);
        } else {
            fDocumentHandler.startElement(element, attributes, modifiedAugs);
            fDocumentHandler.characters(fDefaultValue, null);
            fDocumentHandler.endElement(element, modifiedAugs);
        }
    }
}
