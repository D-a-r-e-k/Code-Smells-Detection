/**
     * A string attribute that represents a sequence of 16 bit units (utf-16
     * encoded characters).
     * <br>If string data is available in the input source, the parser will
     * ignore the character stream and the byte stream and will not attempt
     * to open a URI connection to the system identifier.
     */
public String getStringData() {
    return fData;
}
