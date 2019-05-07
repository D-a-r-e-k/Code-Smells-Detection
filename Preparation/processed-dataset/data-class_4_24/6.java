private Field parseField() throws ParsingException, GrammerException {
    if (scanner.tokenType() == Attribute || scanner.tokenType() == Bracket_Right) {
        return null;
    }
    scanner.mark();
    int acc = 0;
    while (scanner.tokenType() == AccessFlag) {
        acc = acc | Util.getAccessFlag_Field(scanner.token());
        scanner.nextToken();
    }
    String fieldType = scanner.token();
    scanner.nextToken();
    String fieldName = scanner.token();
    scanner.nextToken();
    String maybeEuqal = scanner.token();
    // the next char may be a '=', marks the presence of a constant value attribute  
    if (fieldType.indexOf('(') != -1 || fieldName.indexOf('(') != -1 || maybeEuqal.indexOf('(') != -1) {
        // the presence of '(' marks an method declaration  
        scanner.restore();
        return null;
    }
    ArrayList attributes = new ArrayList(3);
    fieldType = Util.toInnerType(fieldType);
    if (scanner.tokenType() == Equal) {
        scanner.nextToken();
        String constValue = scanner.token();
        int const_index;
        Attribute_ConstantValue con = null;
        switch(fieldType.charAt(0)) {
            case 'B':
            case 'C':
            case 'I':
            case 'S':
            case 'Z':
                const_index = cpl.addInteger(parseInteger(constValue));
                con = new Attribute_ConstantValue(2, const_index);
                break;
            case 'D':
                const_index = cpl.addDouble(parseDouble(constValue));
                con = new Attribute_ConstantValue(2, const_index);
                break;
            case 'F':
                const_index = cpl.addFloat(parseFloat(constValue));
                con = new Attribute_ConstantValue(2, const_index);
                break;
            case 'J':
                const_index = cpl.addLong(parseLong(constValue));
                con = new Attribute_ConstantValue(2, const_index);
                break;
            case 'L':
                if (fieldType.equals("Ljava/lang/String;") == true) {
                    const_index = cpl.addString(Util.parseViewableString(constValue.substring(1, constValue.length() - 1)));
                    // trim  
                    // the  
                    // '"'  
                    con = new Attribute_ConstantValue(2, const_index);
                    break;
                }
            default:
                exception(scanner, "can.not.assign.contant.value.to.this.field.type.only.primitive.types.and.string.allowed");
        }
        con.attribute_name_index = cpl.addUtf8("ConstantValue");
        attributes.add(con);
        scanner.nextToken();
    }
    while (scanner.tokenType() == Attribute) {
        attributes.add(parseAttribute());
    }
    Field ret = new Field(acc, cpl.addUtf8(fieldName), cpl.addUtf8(fieldType), attributes.size(), (Attribute[]) attributes.toArray(new Attribute[attributes.size()]));
    return ret;
}
