public Font getFont(ChainedProperties props) {
    String face = props.getProperty(ElementTags.FACE);
    if (face != null) {
        StringTokenizer tok = new StringTokenizer(face, ",");
        while (tok.hasMoreTokens()) {
            face = tok.nextToken().trim();
            if (face.startsWith("\""))
                face = face.substring(1);
            if (face.endsWith("\""))
                face = face.substring(0, face.length() - 1);
            if (fontImp.isRegistered(face))
                break;
        }
    }
    int style = 0;
    if (props.hasProperty(HtmlTags.I))
        style |= Font.ITALIC;
    if (props.hasProperty(HtmlTags.B))
        style |= Font.BOLD;
    if (props.hasProperty(HtmlTags.U))
        style |= Font.UNDERLINE;
    if (props.hasProperty(HtmlTags.S))
        style |= Font.STRIKETHRU;
    String value = props.getProperty(ElementTags.SIZE);
    float size = 12;
    if (value != null)
        size = Float.parseFloat(value);
    BaseColor color = Markup.decodeColor(props.getProperty("color"));
    String encoding = props.getProperty("encoding");
    if (encoding == null)
        encoding = BaseFont.WINANSI;
    return fontImp.getFont(face, encoding, true, size, style, color);
}
