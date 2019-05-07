// getDocumentSource  
/**
     * The start of the document.
     *
     * @param locator The system identifier of the entity if the entity
     *                 is external, null otherwise.
     * @param encoding The auto-detected IANA encoding name of the entity
     *                 stream. This value will be null in those situations
     *                 where the entity encoding is not auto-detected (e.g.
     *                 internal entities or a document entity that is
     *                 parsed from a java.io.Reader).
     * @param namespaceContext
     *                 The namespace context in effect at the
     *                 start of this document.
     *                 This object represents the current context.
     *                 Implementors of this class are responsible
     *                 for copying the namespace bindings from the
     *                 the current context (and its parent contexts)
     *                 if that information is important.
     * @param augs   Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void startDocument(XMLLocator locator, String encoding, NamespaceContext namespaceContext, Augmentations augs) throws XNIException {
    // call handlers  
    // get initial grammars  
    if (fGrammarPool != null) {
        Grammar[] grammars = fGrammarPool.retrieveInitialGrammarSet(XMLGrammarDescription.XML_DTD);
        final int length = (grammars != null) ? grammars.length : 0;
        for (int i = 0; i < length; ++i) {
            fGrammarBucket.putGrammar((DTDGrammar) grammars[i]);
        }
    }
    fDocLocation = locator;
    fNamespaceContext = namespaceContext;
    if (fDocumentHandler != null) {
        fDocumentHandler.startDocument(locator, encoding, namespaceContext, augs);
    }
}
