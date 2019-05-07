// createUTF8Reader(InputStream):Reader  
/** Create a new UTF-16 reader from the InputStream. **/
private Reader createUTF16Reader(InputStream stream, boolean isBigEndian) {
    if (DEBUG_ENCODINGS) {
        System.out.println("$$$ creating UTF16Reader");
    }
    if (fTempByteBuffer == null) {
        fTempByteBuffer = fLargeByteBufferPool.getBuffer();
    } else if (fTempByteBuffer.length == fBufferSize) {
        fSmallByteBufferPool.returnBuffer(fTempByteBuffer);
        fTempByteBuffer = fLargeByteBufferPool.getBuffer();
    }
    return new UTF16Reader(stream, fTempByteBuffer, isBigEndian, fErrorReporter.getMessageFormatter(XMLMessageFormatter.XML_DOMAIN), fErrorReporter.getLocale());
}
