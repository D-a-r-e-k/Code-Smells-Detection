// startGroup()  
/**
     * The appearance of "#PCDATA" within a group signifying a
     * mixed content model. This method will be the first called
     * following the content model's <code>startGroup()</code>.
     *
     * @param augs Additional information that may include infoset
     *                      augmentations.
     *     
     * @throws XNIException Thrown by handler to signal an error.
     *
     * @see #startGroup
     */
public void pcdata(Augmentations augs) {
    fMixed = true;
    if (fDTDGrammar != null)
        fDTDGrammar.pcdata(augs);
    if (fDTDContentModelHandler != null) {
        fDTDContentModelHandler.pcdata(augs);
    }
}
