/**
     *  The character encoding, if known. The encoding must be a string
     * acceptable for an XML encoding declaration ( section 4.3.3 "Character
     * Encoding in Entities").
     * <br>This attribute has no effect when the application provides a
     * character stream. For other sources of input, an encoding specified
     * by means of this attribute will override any encoding specified in
     * the XML claration or the Text Declaration, or an encoding obtained
     * from a higher level protocol, such as HTTP .
     */
public String getEncoding() {
    return fEncoding;
}
