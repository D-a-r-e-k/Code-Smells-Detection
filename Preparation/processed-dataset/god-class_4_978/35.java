/**
   * Load a set of properties.
   * @param in An <code>InputStream</code> is specified to load properties from a JAR file
   * @deprecated Maintained only for plugins compliance. Use <code>loadXMLProps()</code>
   * instead of this method.
   */
public static void loadProps(InputStream in) {
    try {
        props.load(new BufferedInputStream(in));
        in.close();
    } catch (IOException ioe) {
    }
}
