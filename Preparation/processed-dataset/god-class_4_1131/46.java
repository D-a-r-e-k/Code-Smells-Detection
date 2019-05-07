// createUTF16Reader(InputStream):Reader  
/** Create a new ASCII reader from the InputStream. **/
private Reader createASCIIReader(InputStream stream) {
    if (DEBUG_ENCODINGS) {
        System.out.println("$$$ creating ASCIIReader");
    }
    if (fTempByteBuffer == null) {
        fTempByteBuffer = fSmallByteBufferPool.getBuffer();
    }
    return new ASCIIReader(stream, fTempByteBuffer, fErrorReporter.getMessageFormatter(XMLMessageFormatter.XML_DOMAIN), fErrorReporter.getLocale());
}
