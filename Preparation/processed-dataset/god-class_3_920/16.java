private Class getPrimitiveClass(String className) {
    if (className.equals("java.lang.Integer")) {
        return Integer.TYPE;
    } else if (className.equals("java.lang.Long")) {
        return Long.TYPE;
    } else if (className.equals("java.lang.Short")) {
        return Short.TYPE;
    } else if (className.equals("java.lang.Float")) {
        return Float.TYPE;
    } else if (className.equals("java.lang.Double")) {
        return Double.TYPE;
    } else if (className.equals("java.lang.Character")) {
        return Character.TYPE;
    } else if (className.equals("java.lang.Boolean")) {
        return Boolean.TYPE;
    }
    return null;
}
