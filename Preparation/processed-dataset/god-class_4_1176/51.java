/**
     * Receive notification of an XML comment anywhere in the document. This
     * callback will be used for comments inside or outside the document
     * element, including comments in the external DTD subset (if read).
     * @param ch An array holding the characters in the comment.
     * @param start The starting position in the array.
     * @param length The number of characters to use from the array.
     * @throws org.xml.sax.SAXException The application may raise an exception.
     */
public void comment(char ch[], int start, int length) throws org.xml.sax.SAXException {
    int start_old = start;
    if (m_inEntityRef)
        return;
    if (m_elemContext.m_startTagOpen) {
        closeStartTag();
        m_elemContext.m_startTagOpen = false;
    } else if (m_needToCallStartDocument) {
        startDocumentInternal();
        m_needToCallStartDocument = false;
    }
    try {
        final int limit = start + length;
        boolean wasDash = false;
        if (m_cdataTagOpen)
            closeCDATA();
        if (shouldIndent())
            indent();
        final java.io.Writer writer = m_writer;
        writer.write(COMMENT_BEGIN);
        // Detect occurrences of two consecutive dashes, handle as necessary.  
        for (int i = start; i < limit; i++) {
            if (wasDash && ch[i] == '-') {
                writer.write(ch, start, i - start);
                writer.write(" -");
                start = i + 1;
            }
            wasDash = (ch[i] == '-');
        }
        // if we have some chars in the comment  
        if (length > 0) {
            // Output the remaining characters (if any)  
            final int remainingChars = (limit - start);
            if (remainingChars > 0)
                writer.write(ch, start, remainingChars);
            // Protect comment end from a single trailing dash  
            if (ch[limit - 1] == '-')
                writer.write(' ');
        }
        writer.write(COMMENT_END);
    } catch (IOException e) {
        throw new SAXException(e);
    }
    /*
         * Don't write out any indentation whitespace now,
         * because there may be non-whitespace text after this.
         * 
         * Simply mark that at this point if we do decide
         * to indent that we should 
         * add a newline on the end of the current line before
         * the indentation at the start of the next line.
         */
    m_startNewLine = true;
    // time to generate comment event  
    if (m_tracer != null)
        super.fireCommentEvent(ch, start_old, length);
}
