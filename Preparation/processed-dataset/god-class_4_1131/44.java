// createReader(InputStream,String, Boolean): Reader  
/** Create a new UTF-8 reader from the InputStream. **/
private Reader createUTF8Reader(InputStream stream) {
    if (DEBUG_ENCODINGS) {
        System.out.println("$$$ creating UTF8Reader");
    }
    if (fTempByteBuffer == null) {
        fTempByteBuffer = fSmallByteBufferPool.getBuffer();
    }
    return new UTF8Reader(stream, fTempByteBuffer, fErrorReporter.getMessageFormatter(XMLMessageFormatter.XML_DOMAIN), fErrorReporter.getLocale());
}
