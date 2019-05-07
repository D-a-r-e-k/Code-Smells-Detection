// doctypeDecl(String,String,String,Augmentations) 
/** End document. */
public void endDocument(Augmentations augs) throws XNIException {
    // </body> and </html> have been buffered to consider outside content 
    fIgnoreOutsideContent = true;
    // endElement should not ignore the elements passed from buffer 
    consumeBufferedEndElements();
    // handle empty document 
    if (!fSeenRootElement && !fDocumentFragment) {
        if (fReportErrors) {
            fErrorReporter.reportError("HTML2000", null);
        }
        if (fDocumentHandler != null) {
            fSeenRootElementEnd = false;
            forceStartBody();
            // will force <html> and <head></head> 
            final String body = modifyName("body", fNamesElems);
            fQName.setValues(null, body, body, null);
            callEndElement(fQName, synthesizedAugs());
            final String ename = modifyName("html", fNamesElems);
            fQName.setValues(null, ename, ename, null);
            callEndElement(fQName, synthesizedAugs());
        }
    } else {
        int length = fElementStack.top - fragmentContextStackSize_;
        for (int i = 0; i < length; i++) {
            Info info = fElementStack.pop();
            if (fReportErrors) {
                String ename = info.qname.rawname;
                fErrorReporter.reportWarning("HTML2001", new Object[] { ename });
            }
            if (fDocumentHandler != null) {
                callEndElement(info.qname, synthesizedAugs());
            }
        }
    }
    // call handler 
    if (fDocumentHandler != null) {
        fDocumentHandler.endDocument(augs);
    }
}
