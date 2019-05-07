/**
    * Convert the UML model into a JAG readable XML application file.
    *
    * @param xmiFileName
    * @param outputDir directory where the XML file will be stored.
    * @return the xml File created, if any.
    */
public synchronized File generateXML(String xmiFileName, String outputDir) {
    URL url = null;
    try {
        url = new URL("FILE", EMPTY_STRING, xmiFileName);
    } catch (MalformedURLException e) {
        e.printStackTrace();
    }
    model.readModel(url);
    try {
        checkInterruptStatus();
        return generateXML(model, outputDir);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    return null;
}
