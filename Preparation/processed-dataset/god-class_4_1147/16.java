// emptyElement(QName,XMLAttributes, Augmentations)  
/**
     * Character content.
     *
     * @param text The content.
     * @param augs     Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void characters(XMLString text, Augmentations augs) throws XNIException {
    text = handleCharacters(text);
    // call handlers  
    if (fDocumentHandler != null) {
        if (fNormalizeData && fUnionType) {
            // for union types we can't normalize data  
            // thus we only need to send augs information if any;  
            // the normalized data for union will be send  
            // after normalization is performed (at the endElement())  
            if (augs != null)
                fDocumentHandler.characters(fEmptyXMLStr, augs);
        } else {
            fDocumentHandler.characters(text, augs);
        }
    }
}
