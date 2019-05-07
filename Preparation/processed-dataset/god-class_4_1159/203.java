/**
     * Partial writer for players, so that simple updates to fields such
     * as gold can be brief.
     *
     * @param out The target stream.
     * @param fields The fields to write.
     * @throws XMLStreamException If there are problems writing the stream.
     */
@Override
protected void toXMLPartialImpl(XMLStreamWriter out, String[] fields) throws XMLStreamException {
    toXMLPartialByClass(out, getClass(), fields);
}
