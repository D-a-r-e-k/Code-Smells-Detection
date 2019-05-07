/**
     * @see SerializationHandler#setEscaping(boolean)
     */
public boolean setEscaping(boolean escape) {
    final boolean temp = m_escaping;
    m_escaping = escape;
    return temp;
}
