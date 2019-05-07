// doctypeDecl(String,String,String,Augmentations) 
/** Processing instruction. */
public void processingInstruction(final String target, final XMLString data, final Augmentations augs) throws XNIException {
    final String s = data.toString();
    if (XMLChar.isValidName(s)) {
        final ProcessingInstruction pi = fDocument.createProcessingInstruction(target, s);
        fCurrentNode.appendChild(pi);
    }
}
