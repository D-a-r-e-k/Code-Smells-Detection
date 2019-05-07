void setDescription(final String description) {
    if (description == null)
        throw new IllegalArgumentException("null input: description");
    m_description = description;
}
