/**
    * The character encoding, if known. The encoding must be a string
    * acceptable for an XML encoding declaration ([XML 1.0] section 4.3.3
    * "Character Encoding in Entities"). This attribute has no effect when the
    * application provides a character stream or string data. For other sources
    * of input, an encoding specified by means of this attribute will override
    * any encoding specified in the XML declaration or the Text declaration, or
    * an encoding obtained from a higher level protocol, such as HTTP
    * [IETF RFC 2616].
    */
public String getEncoding() {
    return fEncoding;
}
