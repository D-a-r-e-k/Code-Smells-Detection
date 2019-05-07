private File generateXML(SimpleModel model, String outputDir) throws InterruptedException {
    File file = null;
    Root root = generateConfig(model);
    checkInterruptStatus();
    try {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document doc = builder.newDocument();
        org.w3c.dom.Element skelet = doc.createElement("skelet");
        doc.appendChild(skelet);
        root.getXML(skelet);
        String XMLDoc = com.finalist.jaggenerator.JagGenerator.outXML(doc);
        log.debug("The generated xml project file is: ");
        log.debug(XMLDoc);
        file = new File(outputDir, model.getName() + ".xml");
        checkInterruptStatus();
        OutputStream outputStream = new FileOutputStream(file);
        outputStream.write(XMLDoc.getBytes());
        outputStream.flush();
        outputStream.close();
    } catch (Exception e) {
        e.printStackTrace();
        log("Error writing the file.");
    }
    return file;
}
