private int processLineFeed(final char[] chars, int i, int lastProcessed, final Writer writer) throws IOException {
    if (!m_lineSepUse || (m_lineSepLen == 1 && m_lineSep[0] == CharInfo.S_LINEFEED)) {
    } else {
        writeOutCleanChars(chars, i, lastProcessed);
        writer.write(m_lineSep, 0, m_lineSepLen);
        lastProcessed = i;
    }
    return lastProcessed;
}
