/**
    * The system identifier, a URI reference [IETF RFC 2396], for this output
    *  destination. If the application knows the character encoding of the
    *  object pointed to by the system identifier, it can set the encoding
    *  using the encoding attribute. If the system ID is a relative URI
    *  reference (see section 5 in [IETF RFC 2396]), the behavior is
    *  implementation dependent.
    */
public void setSystemId(String systemId) {
    fSystemId = systemId;
}
