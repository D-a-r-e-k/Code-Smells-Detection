/**
     * Get the output stream where the events will be serialized to.
     *
     * @return reference to the result stream, or null of only a writer was
     * set.
     */
public OutputStream getOutputStream() {
    return m_outputStream;
}
