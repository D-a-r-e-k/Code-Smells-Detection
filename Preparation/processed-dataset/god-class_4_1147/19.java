// endElement(QName, Augmentations)  
/**
    * The start of a CDATA section.
    *
    * @param augs     Additional information that may include infoset augmentations
    *
    * @throws XNIException Thrown by handler to signal an error.
    */
public void startCDATA(Augmentations augs) throws XNIException {
    // REVISIT: what should we do here if schema normalization is on??  
    fInCDATA = true;
    // call handlers  
    if (fDocumentHandler != null) {
        fDocumentHandler.startCDATA(augs);
    }
}
