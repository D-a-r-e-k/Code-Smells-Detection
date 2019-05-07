private void parseClassSignature() throws ParsingException {
    // access flags  
    int acc = 0;
    while (scanner.tokenType() == AccessFlag) {
        acc = acc | Util.getAccessFlag_Class(scanner.token());
        scanner.nextToken();
    }
    if (acc == 0) {
        exception(scanner, "\"class\".expected.here");
    }
    javaClass.access_flags = (short) acc;
    // class name  
    javaClass.this_class = cpl.addClass(scanner.token());
    scanner.nextToken();
    //interfaces and super classes  
    while (scanner.tokenType() != Bracket_Left && scanner.tokenType() != EOF) {
        if ("extends".equals(scanner.token()) == true) {
            scanner.nextToken();
            javaClass.super_class = cpl.addClass(scanner.token());
            scanner.nextToken();
        } else if ("implements".equals(scanner.token()) == true) {
            scanner.nextToken();
            IntegerArray array = new IntegerArray(5);
            while (scanner.tokenType() != Bracket_Left && scanner.tokenType() != EOF) {
                array.add(cpl.addClass(scanner.token()));
                scanner.nextToken();
                if (scanner.tokenType() == Comma) {
                    scanner.nextToken();
                }
            }
            javaClass.interfaces = array.getAll();
            javaClass.interfaces_count = javaClass.interfaces.length;
        } else {
            exception(scanner, "unexpected.character.here");
        }
    }
    scanner.nextToken();
}
