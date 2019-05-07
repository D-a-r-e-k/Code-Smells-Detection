// processingInstruction(String,XMLString,Augmentations) 
/** Comment. */
public void comment(XMLString text, Augmentations augs) throws XNIException {
    Comment comment = fDocument.createComment(text.toString());
    fCurrentNode.appendChild(comment);
}
