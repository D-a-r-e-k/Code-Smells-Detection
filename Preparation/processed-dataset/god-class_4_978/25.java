/**
   * Store the properties on the HD. After having
   * set up the properties, we need to store'em
   * in a file.
   * @deprecated Use <code>saveXMLProps()</code> instead
   */
public static void saveProps() {
    if (usrProps != null) {
        try {
            OutputStream out = new FileOutputStream(usrProps);
            props.store(out, "Jext Properties");
            out.close();
        } catch (IOException io) {
        }
    }
}
