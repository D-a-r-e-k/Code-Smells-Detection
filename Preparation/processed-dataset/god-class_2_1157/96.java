private <T extends FreeColObject> void writeSection(XMLStreamWriter out, String section, Collection<T> items) throws XMLStreamException {
    out.writeStartElement(section);
    for (T item : items) {
        item.toXMLImpl(out);
    }
    out.writeEndElement();
}
