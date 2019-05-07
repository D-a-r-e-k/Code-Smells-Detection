/**
     * Indent at the current element nesting depth.
     * @throws IOException
     */
protected void indent() throws IOException {
    indent(m_elemContext.m_currentElemDepth);
}
