/**
     * The system identifier, a URI reference , for this input source. The
     * system identifier is optional if there is a byte stream or a
     * character stream, but it is still useful to provide one, since the
     * application can use it to resolve relative URIs and can include it in
     * error messages and warnings (the parser will attempt to fetch the
     * ressource identifier by the URI reference only if there is no byte
     * stream or character stream specified).
     * <br>If the application knows the character encoding of the object
     * pointed to by the system identifier, it can register the encoding by
     * setting the encoding attribute.
     * <br>If the system ID is a relative URI reference (see section 5 in ),
     * the behavior is implementation dependent.
     */
public String getSystemId() {
    return fSystemId;
}
