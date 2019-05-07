private Method parseMethod() throws ParsingException, GrammerException {
    if (scanner.tokenType() == Attribute || scanner.tokenType() == Bracket_Right || scanner.tokenType() == EOF) {
        return null;
    }
    Method method = new Method(0, 0, 0, 0, new Attribute[0]);
    LabeledInstructions li;
    ArrayList attributes = new ArrayList(4), codeAttributes;
    parseMethodSignature(method, attributes);
    if (Util.hasMethodBody((short) method.access_flags) == true) {
        codeAttributes = new ArrayList(4);
        li = parseMethodInstructions(method);
        Attribute_Code code = new Attribute_Code();
        code.attribute_name_index = cpl.addUtf8("Code");
        code.codes = li.codes;
        parseMethodAttributes(method, attributes, li, code, codeAttributes);
        code.code_length = li.codeLength;
        code.attributes = (Attribute[]) codeAttributes.toArray(new Attribute[codeAttributes.size()]);
        code.attributes_count = code.attributes.length;
        code.attribute_length = 2 + 2 + 4 + code.code_length + 2 + code.exception_table_length * 8 + 2;
        for (int i = 0; i < code.attributes_count; i++) {
            code.attribute_length += code.attributes[i].attribute_length + 6;
        }
        attributes.add(code);
    } else {
        parseMethodAttributes(method, attributes, null, null, null);
    }
    scanner.nextToken();
    method.attributes = (Attribute[]) attributes.toArray(new Attribute[attributes.size()]);
    method.attributes_count = method.attributes.length;
    return method;
}
