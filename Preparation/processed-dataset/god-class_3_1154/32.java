public static void setField(Field field, Object target, Object value) {
    if (!Modifier.isPublic(field.getModifiers())) {
        field.setAccessible(true);
    }
    try {
        field.set(target, value);
    } catch (IllegalAccessException iae) {
        throw new IllegalArgumentException("Could not set field " + field, iae);
    }
}
