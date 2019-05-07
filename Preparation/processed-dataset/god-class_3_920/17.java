private String convertToJmxArrayType(String type) {
    if (type.equals("[I"))
        return "int[]";
    else if (type.equals("[J"))
        return "long[]";
    else if (type.equals("[B"))
        return "byte[]";
    else if (type.equals("[F"))
        return "float[]";
    else if (type.equals("[C"))
        return "char[]";
    else if (type.equals("[S"))
        return "short[]";
    else if (type.equals("[D"))
        return "double[]";
    else if (type.equals("[Z"))
        return "boolean[]";
    if (type.startsWith("[L") && type.endsWith(";"))
        return (type.substring(2, type.length() - 1) + "[]");
    return type;
}
