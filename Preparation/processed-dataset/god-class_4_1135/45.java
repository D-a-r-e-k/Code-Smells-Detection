// separator(short)  
/**
     * The occurrence count for a child in a children content model or
     * for the mixed content model group.
     * 
     * @param occurrence The occurrence count for the last element
     *                   or group.
     * @param augs Additional information that may include infoset
     *                      augmentations.
     *
     * @throws XNIException Thrown by handler to signal an error.
     *
     * @see #OCCURS_ZERO_OR_ONE
     * @see #OCCURS_ZERO_OR_MORE
     * @see #OCCURS_ONE_OR_MORE
     */
public void occurrence(short occurrence, Augmentations augs) throws XNIException {
    // call handlers  
    if (fDTDGrammar != null)
        fDTDGrammar.occurrence(occurrence, augs);
    if (fDTDContentModelHandler != null) {
        fDTDContentModelHandler.occurrence(occurrence, augs);
    }
}
