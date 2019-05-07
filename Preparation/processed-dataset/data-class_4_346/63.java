/**
     * Tell if, based on space preservation constraints and the doIndent property,
     * if an indent should occur.
     *
     * @return True if an indent should occur.
     */
protected boolean shouldIndent() {
    return m_doIndent && (!m_ispreserve && !m_isprevtext) && m_elemContext.m_currentElemDepth > 0;
}
