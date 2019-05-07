private void parseMajorOrMinor() throws GrammerException, ParsingException {
    String s;
    while (scanner.tokenType() == Attribute) {
        s = scanner.token();
        if (s.indexOf(Constants.ATTRIBUTE_NAME_MAJOR_VERSION) != -1) {
            try {
                javaClass.major_version = parseInteger(s.substring(s.indexOf(':') + 1, s.lastIndexOf(']')).trim());
            } catch (NumberFormatException ne) {
                exception(scanner, "invalid.major.version.definition");
            }
        } else if (s.indexOf(Constants.ATTRIBUTE_NAME_MINOR_VERSION) != -1) {
            try {
                javaClass.minor_version = parseInteger(s.substring(s.indexOf(':') + 1, s.lastIndexOf(']')).trim());
            } catch (NumberFormatException ne) {
                exception(scanner, "invalid.minor.version.definition");
            }
        } else {
            exception(scanner, "unexpected.attribute.here");
        }
        scanner.nextToken();
    }
}
