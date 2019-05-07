/**
	 * like :
	 * [Inner Classes :
	 * access = final class , name = 0 , fullname = jce.TestClass$1 , outername = 0]
	 * @param s
	 * @return
	 */
private Attribute_InnerClasses parseInnerClasses() throws ParsingException, GrammerException {
    Scanner sc;
    //  
    sc = Scanner.partialScanner(scanner.getContent(), scanner.getOffset() + 1, scanner.getLength() - 2, scanner.getColumnNumberStart() + 1, scanner.getLineNumberStart());
    ArrayList ins = new ArrayList();
    int access_flag = 0, inner_name_index, inner_class_info, outer_class_info;
    sc.nextToken();
    if (sc.nextToken() != Colon) {
        exception(sc, "':'.expected");
    }
    sc.nextToken();
    while (sc.tokenType() != EOF) {
        if ("access".equals(sc.token()) == false) {
            exception(sc, "'access'.expected.here");
        }
        if (sc.nextToken() != Equal) {
            exception(sc, "'='.expected.here");
        }
        while (sc.nextToken() == AccessFlag) {
            access_flag = Util.getAccessFlag_Class(sc.token()) | access_flag;
        }
        if (sc.tokenType() != Comma) {
            exception(sc, "','.expected.here");
        }
        sc.nextToken();
        if ("name".equals(sc.token()) == false) {
            exception(sc, "'name'.expected.here");
        }
        if (sc.nextToken() != Equal) {
            exception(sc, "'='.expected.here");
        }
        sc.nextToken();
        if ("0".equals(sc.token()) == true) {
            inner_name_index = 0;
        } else {
            inner_name_index = cpl.addUtf8(sc.token());
        }
        if (sc.nextToken() != Comma) {
            exception(sc, "','.expected.here");
        }
        sc.nextToken();
        if ("fullname".equals(sc.token()) == false) {
            exception(sc, "'fullname'.expected.here");
        }
        if (sc.nextToken() != Equal) {
            exception(sc, "'='.expected.here");
        }
        sc.nextToken();
        inner_class_info = cpl.addClass(sc.token());
        if (sc.nextToken() != Comma) {
            exception(sc, "','.expected.here");
        }
        sc.nextToken();
        if ("outername".equals(sc.token()) == false) {
            exception(sc, "'outername'.expected.here");
        }
        if (sc.nextToken() != Equal) {
            exception(sc, "'='.expected.here");
        }
        sc.nextToken();
        if ("0".equals(sc.token())) {
            outer_class_info = 0;
        } else {
            outer_class_info = cpl.addClass(sc.token());
        }
        sc.nextToken();
        ins.add(new Attribute_InnerClasses.InnerClass(inner_class_info, outer_class_info, inner_name_index, access_flag));
    }
    Attribute_InnerClasses ret = new Attribute_InnerClasses(8 * ins.size() + 2, ins.size(), (Attribute_InnerClasses.InnerClass[]) ins.toArray(new Attribute_InnerClasses.InnerClass[ins.size()]));
    ret.attribute_name_index = cpl.addUtf8("InnerClasses");
    return ret;
}
