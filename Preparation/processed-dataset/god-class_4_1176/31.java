/**
     * Tell if the character escaping should be disabled for the current state.
     *
     * @return true if the character escaping should be disabled.
     */
private boolean isEscapingDisabled() {
    return m_disableOutputEscapingStates.peekOrFalse();
}
