private void parseFields() throws ParsingException, GrammerException {
    ArrayList fields = new ArrayList(10);
    Object field;
    do {
        field = parseField();
        if (field != null) {
            fields.add(field);
        }
    } while (field != null);
    javaClass.fields = (Field[]) fields.toArray(new Field[fields.size()]);
    javaClass.fields_count = javaClass.fields.length;
}
