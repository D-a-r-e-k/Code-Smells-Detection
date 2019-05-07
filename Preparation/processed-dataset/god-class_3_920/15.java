private Class getProperClass(String type) {
    if (type.endsWith("int[]"))
        return (new int[0]).getClass();
    else if (type.endsWith("long[]"))
        return (new long[0]).getClass();
    else if (type.endsWith("byte[]"))
        return (new byte[0]).getClass();
    else if (type.endsWith("float[]"))
        return (new float[0]).getClass();
    else if (type.endsWith("char[]"))
        return (new char[0]).getClass();
    else if (type.endsWith("short[]"))
        return (new short[0]).getClass();
    else if (type.endsWith("double[]"))
        return (new double[0]).getClass();
    else if (type.endsWith("boolean[]"))
        return (new boolean[0]).getClass();
    else if (type.endsWith("String[]")) {
        return (new String[0]).getClass();
    }
    if (type.endsWith("int"))
        return int.class;
    else if (type.endsWith("long"))
        return long.class;
    else if (type.endsWith("byte"))
        return byte.class;
    else if (type.endsWith("float"))
        return float.class;
    else if (type.endsWith("char"))
        return char.class;
    else if (type.endsWith("short"))
        return short.class;
    else if (type.endsWith("double"))
        return double.class;
    else if (type.endsWith("boolean"))
        return boolean.class;
    return null;
}
