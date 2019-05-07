/**
   * Load a set of actions from an XML file.
   * @param in An <code>InputStream</code> is specified to load properties from a JAR file
   * @param fileName The XML filename
   */
public static void loadXMLActions(InputStream in, String fileName) {
    PyActionsReader.read(in, fileName);
}
