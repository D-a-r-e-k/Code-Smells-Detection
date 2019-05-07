/**
     * Serializes the DOM node. Throws an exception only if an I/O
     * exception occured while serializing.
     *
     * @param node Node to serialize.
     * @throws IOException An I/O exception occured while serializing
     */
public void serialize(Node node) throws IOException {
    try {
        TreeWalker walker = new TreeWalker(this);
        walker.traverse(node);
    } catch (org.xml.sax.SAXException se) {
        throw new WrappedRuntimeException(se);
    }
}
