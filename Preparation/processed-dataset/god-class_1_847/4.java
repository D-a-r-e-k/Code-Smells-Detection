public static TypeAdapter adapterFor(Class type) throws UnsupportedOperationException {
    if (type.isPrimitive()) {
        if (type.equals(byte.class))
            return new ByteAdapter();
        if (type.equals(short.class))
            return new ShortAdapter();
        if (type.equals(int.class))
            return new IntAdapter();
        if (type.equals(long.class))
            return new LongAdapter();
        if (type.equals(float.class))
            return new FloatAdapter();
        if (type.equals(double.class))
            return new DoubleAdapter();
        if (type.equals(char.class))
            return new CharAdapter();
        if (type.equals(boolean.class))
            return new BooleanAdapter();
        throw new UnsupportedOperationException("can't yet adapt " + type);
    } else {
        if (type.equals(Byte.class))
            return new ClassByteAdapter();
        if (type.equals(Short.class))
            return new ClassShortAdapter();
        if (type.equals(Integer.class))
            return new ClassIntegerAdapter();
        if (type.equals(Long.class))
            return new ClassLongAdapter();
        if (type.equals(Float.class))
            return new ClassFloatAdapter();
        if (type.equals(Double.class))
            return new ClassDoubleAdapter();
        if (type.equals(Character.class))
            return new ClassCharacterAdapter();
        if (type.equals(Boolean.class))
            return new ClassBooleanAdapter();
        if (type.isArray())
            return new ArrayAdapter();
        return new TypeAdapter();
    }
}
