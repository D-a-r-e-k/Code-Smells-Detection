/**
     * Receive notification of character data.
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
     * @param chars The characters from the XML document.
     * @param start The start position in the array.
     * @param length The number of characters to read from the array.
     * @throws org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see #ignorableWhitespace
     * @see org.xml.sax.Locator
     *
     * @throws org.xml.sax.SAXException
     */
public void characters(final char chars[], final int start, final int length) throws org.xml.sax.SAXException {
    // It does not make sense to continue with rest of the method if the number of   
    // characters to read from array is 0.  
    // Section 7.6.1 of XSLT 1.0 (http://www.w3.org/TR/xslt#value-of) suggest no text node  
    // is created if string is empty.	  
    if (length == 0 || (m_inEntityRef && !m_expandDTDEntities))
        return;
    m_docIsEmpty = false;
    if (m_elemContext.m_startTagOpen) {
        closeStartTag();
        m_elemContext.m_startTagOpen = false;
    } else if (m_needToCallStartDocument) {
        startDocumentInternal();
    }
    if (m_cdataStartCalled || m_elemContext.m_isCdataSection) {
        /* either due to startCDATA() being called or due to 
             * cdata-section-elements atribute, we need this as cdata
             */
        cdata(chars, start, length);
        return;
    }
    if (m_cdataTagOpen)
        closeCDATA();
    if (m_disableOutputEscapingStates.peekOrFalse() || (!m_escaping)) {
        charactersRaw(chars, start, length);
        // time to fire off characters generation event  
        if (m_tracer != null)
            super.fireCharEvent(chars, start, length);
        return;
    }
    if (m_elemContext.m_startTagOpen) {
        closeStartTag();
        m_elemContext.m_startTagOpen = false;
    }
    try {
        int i;
        int startClean;
        // skip any leading whitspace   
        // don't go off the end and use a hand inlined version  
        // of isWhitespace(ch)  
        final int end = start + length;
        int lastDirtyCharProcessed = start - 1;
        // last non-clean character that was processed  
        // that was processed  
        final Writer writer = m_writer;
        boolean isAllWhitespace = true;
        // process any leading whitspace  
        i = start;
        while (i < end && isAllWhitespace) {
            char ch1 = chars[i];
            if (m_charInfo.shouldMapTextChar(ch1)) {
                // The character is supposed to be replaced by a String  
                // so write out the clean whitespace characters accumulated  
                // so far  
                // then the String.  
                writeOutCleanChars(chars, i, lastDirtyCharProcessed);
                String outputStringForChar = m_charInfo.getOutputStringForChar(ch1);
                writer.write(outputStringForChar);
                // We can't say that everything we are writing out is  
                // all whitespace, we just wrote out a String.  
                isAllWhitespace = false;
                lastDirtyCharProcessed = i;
                // mark the last non-clean  
                // character processed  
                i++;
            } else {
                // The character is clean, but is it a whitespace ?  
                switch(ch1) {
                    // TODO: Any other whitespace to consider?  
                    case CharInfo.S_SPACE:
                        // Just accumulate the clean whitespace  
                        i++;
                        break;
                    case CharInfo.S_LINEFEED:
                        lastDirtyCharProcessed = processLineFeed(chars, i, lastDirtyCharProcessed, writer);
                        i++;
                        break;
                    case CharInfo.S_CARRIAGERETURN:
                        writeOutCleanChars(chars, i, lastDirtyCharProcessed);
                        writer.write("&#13;");
                        lastDirtyCharProcessed = i;
                        i++;
                        break;
                    case CharInfo.S_HORIZONAL_TAB:
                        // Just accumulate the clean whitespace  
                        i++;
                        break;
                    default:
                        // The character was clean, but not a whitespace  
                        // so break the loop to continue with this character  
                        // (we don't increment index i !!)  
                        isAllWhitespace = false;
                        break;
                }
            }
        }
        /* If there is some non-whitespace, mark that we may need
             * to preserve this. This is only important if we have indentation on.
             */
        if (i < end || !isAllWhitespace)
            m_ispreserve = true;
        for (; i < end; i++) {
            char ch = chars[i];
            if (m_charInfo.shouldMapTextChar(ch)) {
                // The character is supposed to be replaced by a String  
                // e.g.   '&'  -->  "&amp;"  
                // e.g.   '<'  -->  "&lt;"  
                writeOutCleanChars(chars, i, lastDirtyCharProcessed);
                String outputStringForChar = m_charInfo.getOutputStringForChar(ch);
                writer.write(outputStringForChar);
                lastDirtyCharProcessed = i;
            } else {
                if (ch <= 0x1F) {
                    // Range 0x00 through 0x1F inclusive  
                    //  
                    // This covers the non-whitespace control characters  
                    // in the range 0x1 to 0x1F inclusive.  
                    // It also covers the whitespace control characters in the same way:  
                    // 0x9   TAB  
                    // 0xA   NEW LINE  
                    // 0xD   CARRIAGE RETURN  
                    //  
                    // We also cover 0x0 ... It isn't valid  
                    // but we will output "&#0;"   
                    // The default will handle this just fine, but this  
                    // is a little performance boost to handle the more  
                    // common TAB, NEW-LINE, CARRIAGE-RETURN  
                    switch(ch) {
                        case CharInfo.S_HORIZONAL_TAB:
                            // Leave whitespace TAB as a real character  
                            break;
                        case CharInfo.S_LINEFEED:
                            lastDirtyCharProcessed = processLineFeed(chars, i, lastDirtyCharProcessed, writer);
                            break;
                        case CharInfo.S_CARRIAGERETURN:
                            writeOutCleanChars(chars, i, lastDirtyCharProcessed);
                            writer.write("&#13;");
                            lastDirtyCharProcessed = i;
                            // Leave whitespace carriage return as a real character  
                            break;
                        default:
                            writeOutCleanChars(chars, i, lastDirtyCharProcessed);
                            writer.write("&#");
                            writer.write(Integer.toString(ch));
                            writer.write(';');
                            lastDirtyCharProcessed = i;
                            break;
                    }
                } else if (ch < 0x7F) {
                } else if (ch <= 0x9F) {
                    // Range 0x7F through 0x9F inclusive  
                    // More control characters, including NEL (0x85)  
                    writeOutCleanChars(chars, i, lastDirtyCharProcessed);
                    writer.write("&#");
                    writer.write(Integer.toString(ch));
                    writer.write(';');
                    lastDirtyCharProcessed = i;
                } else if (ch == CharInfo.S_LINE_SEPARATOR) {
                    // LINE SEPARATOR  
                    writeOutCleanChars(chars, i, lastDirtyCharProcessed);
                    writer.write("&#8232;");
                    lastDirtyCharProcessed = i;
                } else if (m_encodingInfo.isInEncoding(ch)) {
                } else {
                    // This is a fallback plan, we should never get here  
                    // but if the character wasn't previously handled  
                    // (i.e. isn't in the encoding, etc.) then what  
                    // should we do?  We choose to write out an entity  
                    writeOutCleanChars(chars, i, lastDirtyCharProcessed);
                    writer.write("&#");
                    writer.write(Integer.toString(ch));
                    writer.write(';');
                    lastDirtyCharProcessed = i;
                }
            }
        }
        // we've reached the end. Any clean characters at the  
        // end of the array than need to be written out?  
        startClean = lastDirtyCharProcessed + 1;
        if (i > startClean) {
            int lengthClean = i - startClean;
            m_writer.write(chars, startClean, lengthClean);
        }
        // For indentation purposes, mark that we've just writen text out  
        m_isprevtext = true;
    } catch (IOException e) {
        throw new SAXException(e);
    }
    // time to fire off characters generation event  
    if (m_tracer != null)
        super.fireCharEvent(chars, start, length);
}
