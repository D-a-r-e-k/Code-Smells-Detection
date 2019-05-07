public Object parse(String s, Class type) throws Exception {
    if (type.equals(String.class)) {
        return s;
    }
    if (type.equals(Date.class)) {
        return DateFormat.getDateInstance().parse(s);
    }
    if (type.equals(ScientificDouble.class)) {
        return ScientificDouble.valueOf(s);
    }
    throw new Exception("can't yet parse " + type);
}
