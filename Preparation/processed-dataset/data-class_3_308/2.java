/**
    * An attribute of a language and binding dependent type that represents a
    * writable stream of bytes. If the application knows the character encoding
    * of the byte stream, it should set the encoding attribute. Setting the
    * encoding in this way will override any encoding specified in an XML
    * declaration in the data.
    */
public void setCharacterStream(Writer characterStream) {
    fCharStream = characterStream;
}
