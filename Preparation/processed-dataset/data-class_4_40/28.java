// endCDATA(Augmentations) 
/** Characters. */
public void characters(final XMLString text, final Augmentations augs) throws XNIException {
    // check for end of document 
    if (fSeenRootElementEnd) {
        return;
    }
    if (fElementStack.top == 0 && !fDocumentFragment) {
        // character before first opening tag 
        lostText_.add(text, augs);
        return;
    }
    // is this text whitespace? 
    boolean whitespace = true;
    for (int i = 0; i < text.length; i++) {
        if (!Character.isWhitespace(text.ch[text.offset + i])) {
            whitespace = false;
            break;
        }
    }
    if (!fDocumentFragment) {
        // handle bare characters 
        if (!fSeenRootElement) {
            if (whitespace) {
                return;
            }
            forceStartBody();
        }
        if (whitespace && (fElementStack.top < 2 || endElementsBuffer_.size() == 1)) {
            // ignore spaces directly within <html> 
            return;
        } else if (!whitespace) {
            Info info = fElementStack.peek();
            if (info.element.code == HTMLElements.HEAD || info.element.code == HTMLElements.HTML) {
                String hname = modifyName("head", fNamesElems);
                String bname = modifyName("body", fNamesElems);
                if (fReportErrors) {
                    fErrorReporter.reportWarning("HTML2009", new Object[] { hname, bname });
                }
                forceStartBody();
            }
        }
    }
    // call handler 
    if (fDocumentHandler != null) {
        fDocumentHandler.characters(text, augs);
    }
}
