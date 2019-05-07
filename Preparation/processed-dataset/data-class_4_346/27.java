/**
     * Normalize the characters, but don't escape.
     *
     * @param ch The characters from the XML document.
     * @param start The start position in the array.
     * @param length The number of characters to read from the array.
     * @param isCData true if a CDATA block should be built around the characters.
     * @param useSystemLineSeparator true if the operating systems 
     * end-of-line separator should be output rather than a new-line character.
     *
     * @throws IOException
     * @throws org.xml.sax.SAXException
     */
void writeNormalizedChars(char ch[], int start, int length, boolean isCData, boolean useSystemLineSeparator) throws IOException, org.xml.sax.SAXException {
    final java.io.Writer writer = m_writer;
    int end = start + length;
    for (int i = start; i < end; i++) {
        char c = ch[i];
        if (CharInfo.S_LINEFEED == c && useSystemLineSeparator) {
            writer.write(m_lineSep, 0, m_lineSepLen);
        } else if (isCData && (!escapingNotNeeded(c))) {
            //                if (i != 0)  
            if (m_cdataTagOpen)
                closeCDATA();
            // This needs to go into a function...   
            if (Encodings.isHighUTF16Surrogate(c)) {
                writeUTF16Surrogate(c, ch, i, end);
                i++;
            } else {
                writer.write("&#");
                String intStr = Integer.toString((int) c);
                writer.write(intStr);
                writer.write(';');
            }
        } else if (isCData && ((i < (end - 2)) && (']' == c) && (']' == ch[i + 1]) && ('>' == ch[i + 2]))) {
            writer.write(CDATA_CONTINUE);
            i += 2;
        } else {
            if (escapingNotNeeded(c)) {
                if (isCData && !m_cdataTagOpen) {
                    writer.write(CDATA_DELIMITER_OPEN);
                    m_cdataTagOpen = true;
                }
                writer.write(c);
            } else if (Encodings.isHighUTF16Surrogate(c)) {
                if (m_cdataTagOpen)
                    closeCDATA();
                writeUTF16Surrogate(c, ch, i, end);
                i++;
            } else {
                if (m_cdataTagOpen)
                    closeCDATA();
                writer.write("&#");
                String intStr = Integer.toString((int) c);
                writer.write(intStr);
                writer.write(';');
            }
        }
    }
}
