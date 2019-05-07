/**
	 * like :
	 * [Exception Table:
	 * start=line73 , end=line78 , handler=line78 , catch_type=java.lang.Exception]
	 * @param s
	 * @param map
	 * @return
	 * @throws ParsingException
	 * @throws GrammerException
	 * TODO: error reporting missing labels
	 */
private Attribute_Code.ExceptionTableItem[] parseExceptionTable(String s, Hashtable map) throws ParsingException, GrammerException {
    Scanner sc;
    sc = Scanner.partialScanner(scanner.getContent(), scanner.getOffset() + 1, scanner.getLength() - 2, scanner.getColumnNumberStart() + 1, scanner.getLineNumberStart());
    ArrayList excs = new ArrayList();
    int start, end, handler, catch_type;
    sc.nextToken();
    if (sc.nextToken() != Colon) {
        exception(sc, "':'.expected");
    }
    sc.nextToken();
    while (sc.tokenType() != EOF) {
        if ("start".equals(sc.token()) == false) {
            exception(sc, "'start'.expected.here");
        }
        if (sc.nextToken() != Equal) {
            exception(scanner, "'='.expected.here");
        }
        sc.nextToken();
        start = getOffset(sc.token(), map, false);
        if (sc.nextToken() != Comma) {
            exception(sc, "','.expected.here");
        }
        sc.nextToken();
        if ("end".equals(sc.token()) == false) {
            exception(sc, "'end'.expected.here");
        }
        if (sc.nextToken() != Equal) {
            exception(sc, "'='.expected.here");
        }
        sc.nextToken();
        end = getOffset(sc.token(), map, false);
        if (sc.nextToken() != Comma) {
            exception(sc, "','.expected.here");
        }
        sc.nextToken();
        if ("handler".equals(sc.token()) == false) {
            exception(sc, "'handler'.expected.here");
        }
        if (sc.nextToken() != Equal) {
            exception(sc, "'='.expected.here");
        }
        sc.nextToken();
        handler = getOffset(sc.token(), map, false);
        if (sc.nextToken() != Comma) {
            exception(sc, "','.expected.here");
        }
        sc.nextToken();
        if ("catch_type".equals(sc.token()) == false) {
            exception(sc, "'catch_type'.expected.here");
        }
        if (sc.nextToken() != Equal) {
            exception(sc, "'='.expected.here");
        }
        sc.nextToken();
        if ("0".equals(sc.token())) {
            catch_type = 0;
        } else {
            catch_type = cpl.addClass(sc.token());
        }
        excs.add(new Attribute_Code.ExceptionTableItem(start, end, handler, catch_type));
        sc.nextToken();
    }
    return (Attribute_Code.ExceptionTableItem[]) excs.toArray(new Attribute_Code.ExceptionTableItem[excs.size()]);
}
