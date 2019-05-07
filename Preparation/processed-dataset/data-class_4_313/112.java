/**
     * Extract location information from an Element node, and create a
     * new SimpleLocator object from such information. Returning null means
     * no information can be retrieved from the element.
     */
public SimpleLocator element2Locator(Element e) {
    if (!(e instanceof ElementImpl))
        return null;
    SimpleLocator l = new SimpleLocator();
    return element2Locator(e, l) ? l : null;
}
