/**
	 * parse method declaration, and the throws clause , if any.
	 * @param method
	 * @throws ParsingException
	 */
private void parseMethodSignature(Method method, ArrayList attributes) throws ParsingException, GrammerException {
    int acc = 0;
    String methodName, retType;
    StringBuffer para = new StringBuffer(15);
    while (scanner.tokenType() == AccessFlag) {
        acc = acc | Util.getAccessFlag_Method(scanner.token());
        scanner.nextToken();
    }
    retType = scanner.token();
    scanner.nextToken();
    methodName = scanner.token();
    scanner.nextToken();
    if (scanner.tokenType() != SBracket_Left) {
        exception(scanner, "'('.expected.here");
    }
    scanner.nextToken();
    if (scanner.tokenType() == SBracket_Right) {
        //void paras  
        para.append("");
    } else {
        while (scanner.tokenType() != EOF && scanner.tokenType() != SBracket_Right) {
            para = para.append(scanner.token());
            if (scanner.nextToken() == Comma) {
                para.append(',');
                scanner.nextToken();
            }
        }
        //validate the next token		  
        if (scanner.tokenType() != SBracket_Right) {
            throw new ParsingException(scanner.getOffset(), "')'.expected.here");
        }
    }
    retType = Util.toInnerType(retType);
    method.descriptor_index = cpl.addUtf8("(" + Util.toInnerParameterTypes(para.toString()) + ")" + retType);
    method.name_index = cpl.addUtf8(methodName);
    method.access_flags = acc;
    scanner.nextToken();
    // throws clause, if any	   
    if ("throws".equals(scanner.token()) == true) {
        IntegerArray thr = new IntegerArray(4);
        while (scanner.tokenType() != Bracket_Left && scanner.tokenType() != EOF) {
            scanner.nextToken();
            thr.add(cpl.addClass(scanner.token()));
            scanner.nextToken();
            if (scanner.tokenType() != Bracket_Left && scanner.tokenType() != Comma) {
                exception(scanner, "invalid.throw.clause");
            }
        }
        Attribute att = new Attribute_Exceptions(2 + 2 * thr.getAll().length, thr.getAll().length, thr.getAll());
        att.attribute_name_index = cpl.addUtf8("Exceptions");
        attributes.add(att);
    } else if (scanner.tokenType() == Bracket_Left) {
    } else {
        exception(scanner, "'{'.expected.here");
    }
    scanner.nextToken();
}
