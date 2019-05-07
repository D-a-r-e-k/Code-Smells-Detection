// setByteStream(InputStream)  
/**
     * Sets the character stream. If the character stream is not already
     * opened when this object is instantiated, then the code that opens 
     * the stream should also set the character stream on this object. 
     * Also, the encoding of the byte stream used by the reader should 
     * also be set on this object, if known.
     *
     * @param charStream The new character stream.
     *
     * @see #setEncoding
     */
public void setCharacterStream(Reader charStream) {
    super.setCharacterStream(charStream);
    if (fInputSource == null) {
        fInputSource = new InputSource();
    }
    fInputSource.setCharacterStream(charStream);
}
