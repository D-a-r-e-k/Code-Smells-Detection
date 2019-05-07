/**
     * Partial reader for players, so that simple updates to fields such
     * as gold can be brief.
     *
     * @param in The input stream with the XML.
     * @throws XMLStreamException If there are problems reading the stream.
     */
@Override
public void readFromXMLPartialImpl(XMLStreamReader in) throws XMLStreamException {
    readFromXMLPartialByClass(in, getClass());
}
