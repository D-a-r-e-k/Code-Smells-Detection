/**
     * Flush the formatter's result stream.
     *
     * @throws org.xml.sax.SAXException
     */
protected final void flushWriter() throws org.xml.sax.SAXException {
    final java.io.Writer writer = m_writer;
    if (null != writer) {
        try {
            if (writer instanceof WriterToUTF8Buffered) {
                if (m_shouldFlush)
                    ((WriterToUTF8Buffered) writer).flush();
                else
                    ((WriterToUTF8Buffered) writer).flushBuffer();
            }
            if (writer instanceof WriterToASCI) {
                if (m_shouldFlush)
                    writer.flush();
            } else {
                // Flush always.   
                // Not a great thing if the writer was created   
                // by this class, but don't have a choice.  
                writer.flush();
            }
        } catch (IOException ioe) {
            throw new org.xml.sax.SAXException(ioe);
        }
    }
}
