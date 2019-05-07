// startEntity(String,XMLResourceIdentifier,String)  
/**
     * This method notifies the end of an entity. The document entity has
     * the pseudo-name of "[xml]" the DTD has the pseudo-name of "[dtd]" 
     * parameter entity names start with '%'; and general entities are just
     * specified by their name.
     * 
     * @param name The name of the entity.
     * @param augs Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void endEntity(String name, Augmentations augs) throws XNIException {
    // keep track of the entity depth  
    fEntityDepth--;
}
