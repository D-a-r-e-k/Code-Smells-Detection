/** 
     * Immediately evaluates an input source and add the new content (e.g. 
     * the output written by an embedded script).
     *
     * @param inputSource The new input source to start evaluating.
     * @see #pushInputSource(XMLInputSource)
     */
public void evaluateInputSource(XMLInputSource inputSource) {
    final Scanner previousScanner = fScanner;
    final short previousScannerState = fScannerState;
    final CurrentEntity previousEntity = fCurrentEntity;
    final Reader reader = getReader(inputSource);
    String encoding = inputSource.getEncoding();
    String publicId = inputSource.getPublicId();
    String baseSystemId = inputSource.getBaseSystemId();
    String literalSystemId = inputSource.getSystemId();
    String expandedSystemId = expandSystemId(literalSystemId, baseSystemId);
    fCurrentEntity = new CurrentEntity(reader, encoding, publicId, baseSystemId, literalSystemId, expandedSystemId);
    setScanner(fContentScanner);
    setScannerState(STATE_CONTENT);
    try {
        do {
            fScanner.scan(false);
        } while (fScannerState != STATE_END_DOCUMENT);
    } catch (final IOException e) {
    }
    setScanner(previousScanner);
    setScannerState(previousScannerState);
    fCurrentEntity = previousEntity;
}
