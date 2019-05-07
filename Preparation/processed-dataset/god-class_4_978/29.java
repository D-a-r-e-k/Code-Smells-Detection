/**
   * Load a set of properties from an XML file.
   * This method, and not the caller, will search for the translated version of the XML.
   * @param in An <code>InputStream</code> is specified to load properties from a JAR file
   * @param fileName The XML filename
   */
public static void loadXMLProps(InputStream in, String fileName) {
    XPropertiesReader.read(in, fileName);
}
