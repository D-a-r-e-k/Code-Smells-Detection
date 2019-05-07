/**
    * Converts a JAG application file (XML) to XMI.
    *
    * @param skeletFileName the JAG application file to be converted to XMI.
    * @param output the file where the XMI will be written to.
    */
public void generateXMI(String skeletFileName, File output) {
    Root root = null;
    File skeletFile = null;
    try {
        skeletFile = new File(skeletFileName);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document doc = null;
        builder = dbf.newDocumentBuilder();
        doc = builder.parse(skeletFile);
        root = new Root(doc);
    } catch (Exception e) {
        e.printStackTrace();
    }
    if (output == null) {
        output = skeletFile.getParentFile();
    }
    generateXMI(root, output);
}
