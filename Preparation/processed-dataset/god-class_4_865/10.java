// getDocumentHandler():XMLDocumentHandler 
// 
// XMLDocumentHandler methods 
// 
// since Xerces-J 2.2.0 
/** Start document. */
public void startDocument(XMLLocator locator, String encoding, NamespaceContext nscontext, Augmentations augs) throws XNIException {
    // reset state 
    fElementStack.top = 0;
    if (fragmentContextStack_ != null) {
        fragmentContextStackSize_ = fragmentContextStack_.length;
        for (int i = 0; i < fragmentContextStack_.length; ++i) {
            final QName name = fragmentContextStack_[i];
            final Element elt = HTMLElements.getElement(name.localpart);
            fElementStack.push(new Info(elt, name));
        }
    } else {
        fragmentContextStackSize_ = 0;
    }
    fSeenAnything = false;
    fSeenDoctype = false;
    fSeenRootElement = false;
    fSeenRootElementEnd = false;
    fSeenHeadElement = false;
    fSeenBodyElement = false;
    // pass on event 
    if (fDocumentHandler != null) {
        XercesBridge.getInstance().XMLDocumentHandler_startDocument(fDocumentHandler, locator, encoding, nscontext, augs);
    }
}
