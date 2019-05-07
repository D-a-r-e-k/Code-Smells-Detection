public static Object getValueFromProtocol(Protocol protocol, Field field) throws IllegalAccessException {
    if (protocol == null || field == null)
        return null;
    if (!Modifier.isPublic(field.getModifiers()))
        field.setAccessible(true);
    return field.get(protocol);
}
