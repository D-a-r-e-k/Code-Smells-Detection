// DOMInputImpl(String,String,String,String,String)  
/**
     * An attribute of a language-binding dependent type that represents a
     * stream of bytes.
     * <br>The parser will ignore this if there is also a character stream
     * specified, but it will use a byte stream in preference to opening a
     * URI connection itself.
     * <br>If the application knows the character encoding of the byte stream,
     * it should set the encoding property. Setting the encoding in this way
     * will override any encoding specified in the XML declaration itself.
     */
public InputStream getByteStream() {
    return fByteStream;
}
