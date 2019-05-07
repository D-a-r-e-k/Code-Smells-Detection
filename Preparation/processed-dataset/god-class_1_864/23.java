// ignorableWhitespace(XMLString,Augmentations) 
/** Start general entity. */
public void startGeneralEntity(String name, XMLResourceIdentifier id, String encoding, Augmentations augs) throws XNIException {
    EntityReference entityRef = fDocument.createEntityReference(name);
    fCurrentNode.appendChild(entityRef);
    fCurrentNode = entityRef;
}
