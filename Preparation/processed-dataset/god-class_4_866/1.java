// 
// Public methods 
// 
/** 
     * Pushes an input source onto the current entity stack. This 
     * enables the scanner to transparently scan new content (e.g. 
     * the output written by an embedded script). At the end of the
     * current entity, the scanner returns where it left off at the
     * time this entity source was pushed.
     * <p>
     * <strong>Note:</strong>
     * This functionality is experimental at this time and is
     * subject to change in future releases of NekoHTML.
     *
     * @param inputSource The new input source to start scanning.
     * @see #evaluateInputSource(XMLInputSource)
     */
public void pushInputSource(XMLInputSource inputSource) {
    final Reader reader = getReader(inputSource);
    fCurrentEntityStack.push(fCurrentEntity);
    String encoding = inputSource.getEncoding();
    String publicId = inputSource.getPublicId();
    String baseSystemId = inputSource.getBaseSystemId();
    String literalSystemId = inputSource.getSystemId();
    String expandedSystemId = expandSystemId(literalSystemId, baseSystemId);
    fCurrentEntity = new CurrentEntity(reader, encoding, publicId, baseSystemId, literalSystemId, expandedSystemId);
}
