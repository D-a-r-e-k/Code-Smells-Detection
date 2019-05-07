private void preprocessConstantValues() throws ParsingException {
    scanner.mark();
    String t;
    while (scanner.nextToken() != EOF) {
        switch(scanner.tokenType()) {
            case String:
                t = scanner.token();
                cpl.addString(Util.parseViewableString(t.substring(1, t.length() - 1)));
                break;
            case Number_Double:
                t = scanner.token();
                cpl.addDouble(parseDouble(t));
            case Number_Long:
                t = scanner.token();
                cpl.addDouble(parseLong(t));
                break;
        }
    }
    scanner.restore();
}
