/**
     *  An attribute of a language-binding dependent type that represents a
     * stream of 16-bit units. Application must encode the stream using
     * UTF-16 (defined in  and Amendment 1 of ).
     * <br>If a character stream is specified, the parser will ignore any byte
     * stream and will not attempt to open a URI connection to the system
     * identifier.
     */
public void setCharacterStream(Reader characterStream) {
    fCharStream = characterStream;
}
