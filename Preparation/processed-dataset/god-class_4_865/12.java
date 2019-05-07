// xmlDecl(String,String,String,Augmentations) 
/** Doctype declaration. */
public void doctypeDecl(String rootElementName, String publicId, String systemId, Augmentations augs) throws XNIException {
    fSeenAnything = true;
    if (fReportErrors) {
        if (fSeenRootElement) {
            fErrorReporter.reportError("HTML2010", null);
        } else if (fSeenDoctype) {
            fErrorReporter.reportError("HTML2011", null);
        }
    }
    if (!fSeenRootElement && !fSeenDoctype) {
        fSeenDoctype = true;
        if (fDocumentHandler != null) {
            fDocumentHandler.doctypeDecl(rootElementName, publicId, systemId, augs);
        }
    }
}
