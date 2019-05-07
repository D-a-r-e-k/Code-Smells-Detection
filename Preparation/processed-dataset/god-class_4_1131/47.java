// createASCIIReader(InputStream):Reader  
/** Create a new ISO-8859-1 reader from the InputStream. **/
private Reader createLatin1Reader(InputStream stream) {
    if (DEBUG_ENCODINGS) {
        System.out.println("$$$ creating Latin1Reader");
    }
    if (fTempByteBuffer == null) {
        fTempByteBuffer = fSmallByteBufferPool.getBuffer();
    }
    return new Latin1Reader(stream, fTempByteBuffer);
}
