public Object parse(String s, Class type) throws Exception {
    if (type.equals(Money.class))
        return new Money(s);
    if (type.equals(Boolean.class))
        return parseCustomBoolean(s);
    return super.parse(s, type);
}
