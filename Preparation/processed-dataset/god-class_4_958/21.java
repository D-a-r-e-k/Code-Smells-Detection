void setExcludesSet(final String[] names) {
    if (names == null)
        throw new IllegalArgumentException("null input: names");
    m_excludesSet = names.length > 0 ? names : null;
}
