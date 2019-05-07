/**
	 * like:jce.TestClass this  start=line0, end=line0, index=0
	 * @param s
	 * @param map
	 * @throws ParsingException
	 * @throws GrammerException
	 */
private Attribute_LocalVariableTable parseLocalVariableTable(String s, Hashtable map) throws ParsingException, GrammerException {
    Scanner sc;
    sc = Scanner.partialScanner(scanner.getContent(), scanner.getOffset() + 1, scanner.getLength() - 2, scanner.getColumnNumberStart() + 1, scanner.getLineNumberStart());
    ArrayList lvts = new ArrayList();
    String type, name, index;
    int start, end;
    sc.nextToken();
    if (sc.nextToken() != Colon) {
        exception(sc, "':'.expected");
    }
    sc.nextToken();
    while (sc.tokenType() != EOF) {
        type = sc.token();
        sc.nextToken();
        name = sc.token();
        sc.nextToken();
        if ("start".equals(sc.token()) == false) {
            exception(sc, "'start'.expected.here");
        }
        if (sc.nextToken() != Equal) {
            exception(sc, "'='.expected.here");
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
        end = getOffset(sc.token(), map, true);
        if (sc.nextToken() != Comma) {
            exception(sc, "','.expected.here");
        }
        sc.nextToken();
        if ("index".equals(sc.token()) == false) {
            exception(sc, "'index'.expected.here");
        }
        if (sc.nextToken() != Equal) {
            exception(sc, "'='.expected.here");
        }
        if (sc.nextToken() != Number_Integer) {
            exception(sc, "local.variable.index.expected.here");
        }
        index = sc.token();
        lvts.add(new Attribute_LocalVariableTable.LocalVariable(start, end - start, cpl.addUtf8(name), cpl.addUtf8(Util.toInnerType(type)), parseInteger(index)));
        sc.nextToken();
    }
    Attribute_LocalVariableTable.LocalVariable[] lvs = (Attribute_LocalVariableTable.LocalVariable[]) lvts.toArray(new Attribute_LocalVariableTable.LocalVariable[lvts.size()]);
    Attribute_LocalVariableTable lvt = new Attribute_LocalVariableTable(10 * lvs.length + 2, lvs.length, lvs);
    lvt.attribute_name_index = cpl.addUtf8("LocalVariableTable");
    return lvt;
}
