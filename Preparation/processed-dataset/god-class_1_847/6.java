public Object get() throws IllegalAccessException, InvocationTargetException {
    if (field != null) {
        return field.get(target);
    }
    if (method != null) {
        return invoke();
    }
    return null;
}
