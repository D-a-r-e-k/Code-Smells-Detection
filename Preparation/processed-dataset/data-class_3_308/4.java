/**
    * Depending on the language binding in use, this attribute may not be
    * available. An attribute of a language and binding dependent type that
    * represents a writable stream to which 16-bit units can be output. The
    * application must encode the stream using UTF-16 (defined in [Unicode] and
    *  Amendment 1 of [ISO/IEC 10646]).
    */
public void setByteStream(OutputStream byteStream) {
    fByteStream = byteStream;
}
