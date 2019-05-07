// setCharacterStream(Reader)  
/**
     * Sets the encoding of the stream.
     *
     * @param encoding The new encoding.
     */
public void setEncoding(String encoding) {
    super.setEncoding(encoding);
    if (fInputSource == null) {
        fInputSource = new InputSource();
    }
    fInputSource.setEncoding(encoding);
}
