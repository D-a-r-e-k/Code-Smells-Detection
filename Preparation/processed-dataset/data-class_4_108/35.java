/* Use the JAXB JDK1.4 parser to serialize to XML. */
public static String outXML(Document doc) {
    try {
        DOMSource domSource = new DOMSource(doc);
        StringWriter sw = new StringWriter();
        StreamResult streamResult = new StreamResult(sw);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.transform(domSource, streamResult);
        return sw.toString();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
