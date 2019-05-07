/**
     * Prints <var>n</var> spaces.
     * @param n         Number of spaces to print.
     *
     * @throws org.xml.sax.SAXException if an error occurs when writing.
     */
private void printSpace(int n) throws IOException {
    final java.io.Writer writer = m_writer;
    for (int i = 0; i < n; i++) {
        writer.write(' ');
    }
}
