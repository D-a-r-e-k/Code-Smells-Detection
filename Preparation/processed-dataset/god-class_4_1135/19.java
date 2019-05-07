// startDTD(XMLLocator)  
/**
     * Characters within an IGNORE conditional section.
     *
     * @param text The ignored text.
     * @param augs Additional information that may include infoset
     *                      augmentations.
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void ignoredCharacters(XMLString text, Augmentations augs) throws XNIException {
    // ignored characters in DTD  
    if (fDTDGrammar != null)
        fDTDGrammar.ignoredCharacters(text, augs);
    if (fDTDHandler != null) {
        fDTDHandler.ignoredCharacters(text, augs);
    }
}
