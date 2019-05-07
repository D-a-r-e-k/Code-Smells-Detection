/**
     * Puts all elments with an "id" or an "xml:id" attribute into the
     * hashtable {@code identifiedElements}.
     */
private void identifyElements(IXMLElement elem) {
    identifiedElements.put(elem.getAttribute("id", ""), elem);
    identifiedElements.put(elem.getAttribute("xml:id", ""), elem);
    for (IXMLElement child : elem.getChildren()) {
        identifyElements(child);
    }
}
