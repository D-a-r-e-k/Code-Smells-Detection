private void parseMethods() throws ParsingException, GrammerException {
    ArrayList methods = new ArrayList(10);
    Object method;
    do {
        method = parseMethod();
        if (method != null) {
            methods.add(method);
        }
    } while (method != null);
    javaClass.methods = (Method[]) methods.toArray(new Method[methods.size()]);
    javaClass.methods_count = javaClass.methods.length;
}
