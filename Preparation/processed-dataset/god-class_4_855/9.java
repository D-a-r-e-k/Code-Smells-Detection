/**
	 * this method will parse method attribute: Deprecated, Synthetic
	 * and some of the attributes belongs to code:  Max Locals, Max Stack,Local variale table, Exception table
	 * 
	 * @param method
	 * @param attributes
	 */
private void parseMethodAttributes(Method method, ArrayList attributes, LabeledInstructions li, Attribute_Code code, ArrayList codeAttributes) throws GrammerException, ParsingException {
    String temp;
    while (scanner.tokenType() == Attribute) {
        temp = scanner.token();
        if (temp.indexOf(Constants.ATTRIBUTE_NAME_LOCAL_VARIABLE) != -1) {
            codeAttributes.add(parseLocalVariableTable(temp, li.labels));
            scanner.nextToken();
        } else if (temp.indexOf(Constants.ATTRIBUTE_NAME_EXCEPTION_TABLE) != -1) {
            code.exception_table = parseExceptionTable(temp, li.labels);
            code.exception_table_length = code.exception_table.length;
            scanner.nextToken();
        } else if (temp.indexOf(Constants.ATTRIBUTE_NAME_MAX_STACK) != -1) {
            parseMaxStackOrLocals(code);
            scanner.nextToken();
        } else if (temp.indexOf(Constants.ATTRIBUTE_NAME_MAX_LOCAL) != -1) {
            parseMaxStackOrLocals(code);
            scanner.nextToken();
        } else if (temp.indexOf(Constants.ATTRIBUTE_NAME_DEPRECATED) != -1) {
            attributes.add(parseAttribute());
        } else if (temp.indexOf(Constants.ATTRIBUTE_NAME_SYNTHETIC) != -1) {
            attributes.add(parseAttribute());
        } else if (temp.indexOf(Constants.ATTRIBUTE_NAME_LINE_NUMBER_TABLE) != -1) {
            scanner.nextToken();
            if (false) {
                parseLineNumbers(null);
            }
        } else {
            exception(scanner, "unexpected.attribute." + scanner.token());
        }
    }
}
