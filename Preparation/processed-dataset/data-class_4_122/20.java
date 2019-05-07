void setRequiresSet(final String[] names) {
    if (names == null)
        throw new IllegalArgumentException("null input: names");
    m_requiresSet = names.length > 0 ? names : null;
}
