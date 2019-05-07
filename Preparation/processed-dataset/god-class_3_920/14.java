private String convertToWrapperType(String type) {
    if (type.endsWith("int"))
        return "java.lang.Integer";
    else if (type.endsWith("int[]"))
        return "[Ljava.lang.Integer;";
    else if (type.endsWith("long"))
        return "java.lang.Long";
    else if (type.endsWith("long[]"))
        return "[Ljava.lang.Long;";
    else if (type.endsWith("byte"))
        return "java.lang.Byte";
    else if (type.endsWith("byte[]"))
        return "[Ljava.lang.Byte;";
    else if (type.endsWith("float"))
        return "java.lang.Float";
    else if (type.endsWith("float[]"))
        return "[Ljava.lang.Float;";
    else if (type.endsWith("char"))
        return "java.lang.Character";
    else if (type.endsWith("char[]"))
        return "[Ljava.lang.Character;";
    else if (type.endsWith("short"))
        return "java.lang.Short";
    else if (type.endsWith("short[]"))
        return "[Ljava.lang.Short;";
    else if (type.endsWith("double"))
        return "java.lang.Double";
    else if (type.endsWith("double[]"))
        return "[Ljava.lang.Double;";
    else if (type.endsWith("boolean"))
        return "java.lang.Boolean";
    else if (type.endsWith("boolean[]"))
        return "[Ljava.lang.Boolean;";
    return null;
}
