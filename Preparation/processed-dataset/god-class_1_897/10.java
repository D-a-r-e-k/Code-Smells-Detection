public static void main(String[] args) throws Exception {
    Map map = new HashMap();
    map.put("falseArg", Boolean.FALSE);
    map.put("trueArg", Boolean.TRUE);
    map.put("stringValue", "someString");
    new JavaFileGenerator(args[0], map).generate(new PrintWriter(args[1]));
}
