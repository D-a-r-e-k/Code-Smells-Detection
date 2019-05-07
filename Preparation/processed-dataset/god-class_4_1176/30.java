/**
     * Receive notification of cdata.
     *
     * <p>The Parser will call this method to report each chunk of
     * character data.  SAX parsers may return all contiguous character
     * data in a single chunk, or they may split it into several
     * chunks; however, all of the characters in any single event
     * must come from the same external entity, so that the Locator
     * provides useful information.</p>
     *
     * <p>The application must not attempt to read from the array
     * outside of the specified range.</p>
     *
     * <p>Note that some parsers will report whitespace using the
     * ignorableWhitespace() method rather than this one (validating
     * parsers must do so).</p>
     *
     * @param ch The characters from the XML document.
     * @param start The start position in the array.
     * @param length The number of characters to read from the array.
     * @throws org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see #ignorableWhitespace
     * @see org.xml.sax.Locator
     *
     * @throws org.xml.sax.SAXException
     */
protected void cdata(char ch[], int start, final int length) throws org.xml.sax.SAXException {
    try {
        final int old_start = start;
        if (m_elemContext.m_startTagOpen) {
            closeStartTag();
            m_elemContext.m_startTagOpen = false;
        }
        m_ispreserve = true;
        if (shouldIndent())
            indent();
        boolean writeCDataBrackets = (((length >= 1) && escapingNotNeeded(ch[start])));
        /* Write out the CDATA opening delimiter only if
             * we are supposed to, and if we are not already in
             * the middle of a CDATA section  
             */
        if (writeCDataBrackets && !m_cdataTagOpen) {
            m_writer.write(CDATA_DELIMITER_OPEN);
            m_cdataTagOpen = true;
        }
        // writer.write(ch, start, length);  
        if (isEscapingDisabled()) {
            charactersRaw(ch, start, length);
        } else
            writeNormalizedChars(ch, start, length, true, m_lineSepUse);
        /* used to always write out CDATA closing delimiter here,
             * but now we delay, so that we can merge CDATA sections on output.    
             * need to write closing delimiter later
             */
        if (writeCDataBrackets) {
            /* if the CDATA section ends with ] don't leave it open
                 * as there is a chance that an adjacent CDATA sections
                 * starts with ]>.  
                 * We don't want to merge ]] with > , or ] with ]> 
                 */
            if (ch[start + length - 1] == ']')
                closeCDATA();
        }
        // time to fire off CDATA event  
        if (m_tracer != null)
            super.fireCDATAEvent(ch, old_start, length);
    } catch (IOException ioe) {
        throw new org.xml.sax.SAXException(Utils.messages.createMessage(MsgKey.ER_OIERROR, null), ioe);
    }
}
