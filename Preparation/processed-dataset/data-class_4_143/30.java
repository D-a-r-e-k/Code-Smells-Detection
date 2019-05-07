/**
   * Load a set of properties from an XML file.
   * It is provided for when the caller already provides the translated file(for instance for the plugin
   * translation); in this case toTranslate must be true, otherwise it will be translated the default way.
   * @param in An <code>InputStream</code> is specified to load properties from a JAR file
   * @param fileName The XML filename
   * @since Jext3.2pre1
   */
public static void loadXMLProps(InputStream in, String fileName, boolean toTranslate) {
    XPropertiesReader.read(in, fileName, toTranslate);
}
