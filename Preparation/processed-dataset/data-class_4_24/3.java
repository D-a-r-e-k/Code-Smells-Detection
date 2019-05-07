private void parseClass() throws ParsingException, GrammerException {
    scanner.nextToken();
    if (scanner.tokenType() == Attribute) {
        parseMajorOrMinor();
    }
    parseClassSignature();
    parseFields();
    parseMethods();
    parseClassAttributes();
    if (scanner.tokenType() != Bracket_Right) {
        exception(scanner, "'}'.expected.here");
    }
    if (scanner.nextToken() != EOF) {
        exception(scanner, "end.of.class.expected.here");
    }
    javaClass.constantPool = cpl.getConstantPool();
    javaClass.constant_pool_count = javaClass.constantPool.getConstantPoolCount();
}
