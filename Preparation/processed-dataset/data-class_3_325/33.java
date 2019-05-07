public static Object getField(Field field, Object target) {
    if (!Modifier.isPublic(field.getModifiers())) {
        field.setAccessible(true);
    }
    try {
        return field.get(target);
    } catch (IllegalAccessException iae) {
        throw new IllegalArgumentException("Could not get field " + field, iae);
    }
}
