// setProperty(String,Object) 
// 
// XMLDocumentScanner methods 
// 
/** Sets the input source. */
public void setInputSource(XMLInputSource source) throws IOException {
    // reset state 
    fElementCount = 0;
    fElementDepth = -1;
    fByteStream = null;
    fCurrentEntityStack.removeAllElements();
    fBeginLineNumber = 1;
    fBeginColumnNumber = 1;
    fBeginCharacterOffset = 0;
    fEndLineNumber = fBeginLineNumber;
    fEndColumnNumber = fBeginColumnNumber;
    fEndCharacterOffset = fBeginCharacterOffset;
    // reset encoding information 
    fIANAEncoding = fDefaultIANAEncoding;
    fJavaEncoding = fIANAEncoding;
    // get location information 
    String encoding = source.getEncoding();
    String publicId = source.getPublicId();
    String baseSystemId = source.getBaseSystemId();
    String literalSystemId = source.getSystemId();
    String expandedSystemId = expandSystemId(literalSystemId, baseSystemId);
    // open stream 
    Reader reader = source.getCharacterStream();
    if (reader == null) {
        InputStream inputStream = source.getByteStream();
        if (inputStream == null) {
            URL url = new URL(expandedSystemId);
            inputStream = url.openStream();
        }
        fByteStream = new PlaybackInputStream(inputStream);
        String[] encodings = new String[2];
        if (encoding == null) {
            fByteStream.detectEncoding(encodings);
        } else {
            encodings[0] = encoding;
        }
        if (encodings[0] == null) {
            encodings[0] = fDefaultIANAEncoding;
            if (fReportErrors) {
                fErrorReporter.reportWarning("HTML1000", null);
            }
        }
        if (encodings[1] == null) {
            encodings[1] = EncodingMap.getIANA2JavaMapping(encodings[0].toUpperCase());
            if (encodings[1] == null) {
                encodings[1] = encodings[0];
                if (fReportErrors) {
                    fErrorReporter.reportWarning("HTML1001", new Object[] { encodings[0] });
                }
            }
        }
        fIANAEncoding = encodings[0];
        fJavaEncoding = encodings[1];
        /* PATCH: Asgeir Asgeirsson */
        fIso8859Encoding = fIANAEncoding == null || fIANAEncoding.toUpperCase().startsWith("ISO-8859") || fIANAEncoding.equalsIgnoreCase(fDefaultIANAEncoding);
        encoding = fIANAEncoding;
        reader = new InputStreamReader(fByteStream, fJavaEncoding);
    }
    fCurrentEntity = new CurrentEntity(reader, encoding, publicId, baseSystemId, literalSystemId, expandedSystemId);
    // set scanner and state 
    setScanner(fContentScanner);
    setScannerState(STATE_START_DOCUMENT);
}
