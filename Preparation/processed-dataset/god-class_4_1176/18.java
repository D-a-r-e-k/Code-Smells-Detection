/**
     * Might print a newline character and the indentation amount
     * of the given depth.
     * 
     * @param depth the indentation depth (element nesting depth)
     *
     * @throws org.xml.sax.SAXException if an error occurs during writing.
     */
protected void indent(int depth) throws IOException {
    if (m_startNewLine)
        outputLineSep();
    /* For m_indentAmount > 0 this extra test might be slower
         * but Xalan's default value is 0, so this extra test
         * will run faster in that situation.
         */
    if (m_indentAmount > 0)
        printSpace(depth * m_indentAmount);
}
