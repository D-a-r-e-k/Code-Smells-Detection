/**
      * Sets the character encoding coming from the xsl:output encoding stylesheet attribute.
      * @param encoding the character encoding
      */
public void setEncoding(String encoding) {
    setOutputProperty(OutputKeys.ENCODING, encoding);
}
