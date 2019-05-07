/**
   * Saves the user's properties to a file using the XML specifications.
   * @param description is a <code>String</code> containing a little
   * description of the properties file. This String is stored on
   * topmost of the user's properties file. Can be set to
   * <code>null</code>.
   */
public static void saveXMLProps(String description) {
    saveXMLProps(usrProps, description);
}
