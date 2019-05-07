private void load(InputStream in) {
    try {
        XMLStreamReader xsr = XMLInputFactory.newInstance().createXMLStreamReader(in);
        xsr.nextTag();
        readFromXML(xsr);
    } catch (Exception e) {
        logger.log(Level.WARNING, "Load exception", e);
        throw new RuntimeException("Error parsing specification: " + e.getMessage());
    }
}
