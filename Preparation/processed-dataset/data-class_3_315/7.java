// setSystemId(String)  
/**
     * Sets the byte stream. If the byte stream is not already opened
     * when this object is instantiated, then the code that opens the
     * stream should also set the byte stream on this object. Also, if
     * the encoding is auto-detected, then the encoding should also be
     * set on this object.
     *
     * @param byteStream The new byte stream.
     */
public void setByteStream(InputStream byteStream) {
    super.setByteStream(byteStream);
    if (fInputSource == null) {
        fInputSource = new InputSource();
    }
    fInputSource.setByteStream(byteStream);
}
