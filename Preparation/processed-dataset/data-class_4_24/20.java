private void parseMaxStackOrLocals(Attribute_Code code) throws ParsingException {
    Scanner sc = Scanner.partialScanner(scanner.getContent(), scanner.getOffset() + 1, scanner.getLength() - 2, scanner.getColumnNumberStart() + 1, scanner.getLineNumberStart());
    sc.nextToken();
    if (sc.token().equals(Constants.ATTRIBUTE_NAME_MAX_STACK) == true) {
        if (sc.nextToken() != Colon) {
            exception(sc, "':'.expected.here");
        }
        if (sc.nextToken() != Number_Integer) {
            exception(sc, "invalid.max.stack.value");
        }
        code.max_stack = parseInteger(sc.token());
    } else if (sc.token().equals(Constants.ATTRIBUTE_NAME_MAX_LOCAL) == true) {
        if (sc.nextToken() != Colon) {
            exception(sc, "':'.expected.here");
        }
        if (sc.nextToken() != Number_Integer) {
            exception(sc, "invalid.max.local.value");
        }
        code.max_locals = parseInteger(sc.token());
    }
}
