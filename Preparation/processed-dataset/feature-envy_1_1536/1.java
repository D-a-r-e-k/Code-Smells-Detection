/**
     * Reads a &lt;styles&gt; element from the specified
     * XML element.
     * <p>
     * The styles element contains common styles.
     *
     *
     * @param elem A &lt;styles&gt; element.
     */
private void readStylesElement(IXMLElement elem) throws IOException {
    readStylesChildren(elem, commonStyles);
}
