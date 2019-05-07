private Attribute parseAttribute() throws GrammerException, ParsingException {
    String s = scanner.token();
    Attribute att;
    if (s.indexOf(Constants.ATTRIBUTE_NAME_DEPRECATED) != -1) {
        att = new Attribute_Deprecated();
        att.attribute_name_index = cpl.addUtf8("Deprecated");
        scanner.nextToken();
        return att;
    } else if (s.indexOf(Constants.ATTRIBUTE_NAME_SYNTHETIC) != -1) {
        att = new Attribute_Synthetic();
        att.attribute_name_index = cpl.addUtf8("Synthetic");
        scanner.nextToken();
        return att;
    } else if (s.indexOf(Constants.ATTRIBUTE_NAME_SOURCE_FILE) != -1) {
        att = new Attribute_SourceFile(2, cpl.addUtf8(s.substring(s.lastIndexOf(':') + 1, s.length() - 1).trim()));
        att.attribute_name_index = cpl.addUtf8("SourceFile");
        scanner.nextToken();
        return att;
    } else {
        exception(scanner, "can.not.process.attribute");
    }
    return null;
}
