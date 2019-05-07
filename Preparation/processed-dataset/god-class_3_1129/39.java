/**
     * {@inheritDoc}
     * <p>
     * Clears the Header Manager property so subsequent loops don't keep merging more elements
     */
@Override
public void clearTestElementChildren() {
    removeProperty(HEADER_MANAGER);
}
