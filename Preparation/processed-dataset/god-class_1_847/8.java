public Object invoke() throws IllegalAccessException, InvocationTargetException {
    Object params[] = {};
    return method.invoke(target, params);
}
