public static boolean isSetPropertyMethod(Method method) {
    return (method.getName().startsWith("set") && method.getReturnType() == java.lang.Void.TYPE && method.getParameterTypes().length == 1);
}
