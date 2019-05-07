// Serialization 
/**
     * Creates an XML-representation of this object.
     *
     * @param document The <code>Document</code> in which the
     *            XML-representation should be created.
     * @return The XML-representation.
     */
public Element toXMLElement(Document document) {
    Element element = document.createElement(getXMLElementTagName());
    element.setAttribute(FreeColObject.ID_ATTRIBUTE, colony.getId());
    return element;
}
