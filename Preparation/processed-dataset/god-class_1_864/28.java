// endCDATA(Augmentations) 
/** End element. */
public void endElement(QName element, Augmentations augs) throws XNIException {
    fCurrentNode = fCurrentNode.getParentNode();
}
