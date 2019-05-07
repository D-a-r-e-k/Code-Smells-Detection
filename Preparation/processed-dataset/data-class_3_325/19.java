public static Object getValueFromProtocol(Protocol protocol, String field_name) throws IllegalAccessException {
    if (protocol == null || field_name == null)
        return null;
    Field field = Util.getField(protocol.getClass(), field_name);
    return field != null ? getValueFromProtocol(protocol, field) : null;
}
