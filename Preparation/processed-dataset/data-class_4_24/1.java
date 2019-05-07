public JavaClass parse() throws ParsingException {
    javaClass = new JavaClass();
    cpl = new ConstantPoolGenerator();
    if (precompile == true) {
        preprocessConstantValues();
    }
    parseClass();
    return javaClass;
}
