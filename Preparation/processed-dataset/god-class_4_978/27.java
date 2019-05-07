/**
   * Saves the user's properties to a file using the XML specifications.
   * @param userProps is the path to the file in which properties will
   * be stored. If it is set to <code>null</code>, properties are not
   * saved at all.
   * @param description is a <code>String</code> containing a little
   * description of the properties file. This String is stored on
   * topmost of the user's properties file. Can be set to
   * <code>null</code>.
   */
public static void saveXMLProps(String userProps, String description) {
    if (userProps != null) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(userProps));
            String _out = new String("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            out.write(_out, 0, _out.length());
            out.newLine();
            _out = new String("<!DOCTYPE xproperties SYSTEM \"xproperties.dtd\" >");
            out.write(_out, 0, _out.length());
            out.newLine();
            _out = "<!-- Last save: " + (new Date()).toString() + " -->";
            out.write(_out, 0, _out.length());
            out.newLine();
            if (description == null)
                description = new String("Properties");
            description = "<!-- " + description + " -->";
            out.write(description, 0, description.length());
            out.newLine();
            out.newLine();
            _out = new String("<xproperties>");
            out.write(_out, 0, _out.length());
            out.newLine();
            setProperty("properties.version", BUILD);
            char c = '\0';
            StringBuffer buf;
            Enumeration k = props.keys();
            Enumeration e = props.elements();
            for (; e.hasMoreElements(); ) {
                buf = new StringBuffer("  <property name=\"");
                buf.append(k.nextElement());
                buf.append("\" value=\"");
                String _e = (String) e.nextElement();
                for (int i = 0; i < _e.length(); i++) {
                    switch(c = _e.charAt(i)) {
                        case '\\':
                            buf.append('\\');
                            buf.append('\\');
                            break;
                        case '\'':
                            buf.append("&apos;");
                            break;
                        case '&':
                            buf.append("&amp;");
                            break;
                        case '\"':
                            buf.append("&#34;");
                            break;
                        case '\n':
                            buf.append('\\');
                            buf.append('n');
                            break;
                        case '\r':
                            buf.append('\\');
                            buf.append('r');
                            break;
                        default:
                            buf.append(c);
                    }
                }
                buf.append("\" />");
                //for (int i = 0; i < buf.length(); i++) 
                //  out.write(buf.charAt(i)); 
                out.write(buf.toString(), 0, buf.length());
                out.newLine();
            }
            _out = new String("</xproperties>");
            out.write(_out, 0, _out.length());
            out.close();
        } catch (IOException io) {
        }
    }
}
