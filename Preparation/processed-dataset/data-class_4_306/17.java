// comment(XMLString)  
/**
     * A processing instruction. Processing instructions consist of a
     * target name and, optionally, text data. The data is only meaningful
     * to the application.
     * <p>
     * Typically, a processing instruction's data will contain a series
     * of pseudo-attributes. These pseudo-attributes follow the form of
     * element attributes but are <strong>not</strong> parsed or presented
     * to the application as anything other than text. The application is
     * responsible for parsing the data.
     * 
     * @param target The target.
     * @param data   The data or null if none specified.     
     * @param augs   Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void processingInstruction(String target, XMLString data, Augmentations augs) throws XNIException {
    // call handlers  
    if (fDTDGrammar != null)
        fDTDGrammar.processingInstruction(target, data, augs);
    if (fDTDHandler != null) {
        fDTDHandler.processingInstruction(target, data, augs);
    }
}
