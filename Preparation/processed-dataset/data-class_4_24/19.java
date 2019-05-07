private void parseClassAttributes() throws GrammerException, ParsingException {
    String s;
    ArrayList attributes = new ArrayList(4);
    int colonIndex, nameIndex;
    while (scanner.tokenType() == Attribute) {
        s = scanner.token();
        colonIndex = s.indexOf(':');
        nameIndex = s.indexOf(Constants.ATTRIBUTE_NAME_INNER_CLASSES);
        if (nameIndex != -1 && nameIndex < colonIndex) {
            // this is necessary, or  [SourceFile : Attribute_InnerClasses.java] will be parsed as innerclass  
            attributes.add(parseInnerClasses());
            scanner.nextToken();
        } else {
            attributes.add(parseAttribute());
        }
    }
    javaClass.attributes = (Attribute[]) attributes.toArray(new Attribute[attributes.size()]);
    javaClass.attributes_count = attributes.size();
}
