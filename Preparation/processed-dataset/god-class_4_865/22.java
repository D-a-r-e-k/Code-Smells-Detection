// emptyElement(QName,XMLAttributes,Augmentations) 
/** Start entity. */
public void startGeneralEntity(String name, XMLResourceIdentifier id, String encoding, Augmentations augs) throws XNIException {
    fSeenAnything = true;
    // check for end of document 
    if (fSeenRootElementEnd) {
        return;
    }
    // insert body, if needed 
    if (!fDocumentFragment) {
        boolean insertBody = !fSeenRootElement;
        if (!insertBody) {
            Info info = fElementStack.peek();
            if (info.element.code == HTMLElements.HEAD || info.element.code == HTMLElements.HTML) {
                String hname = modifyName("head", fNamesElems);
                String bname = modifyName("body", fNamesElems);
                if (fReportErrors) {
                    fErrorReporter.reportWarning("HTML2009", new Object[] { hname, bname });
                }
                fQName.setValues(null, hname, hname, null);
                endElement(fQName, synthesizedAugs());
                insertBody = true;
            }
        }
        if (insertBody) {
            forceStartBody();
        }
    }
    // call handler 
    if (fDocumentHandler != null) {
        fDocumentHandler.startGeneralEntity(name, id, encoding, augs);
    }
}
