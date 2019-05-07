private boolean isPrimitiveArrayDataType(String type) {
    if (type.endsWith("int[]"))
        return true;
    else if (type.endsWith("long[]"))
        return true;
    else if (type.endsWith("byte[]"))
        return true;
    else if (type.endsWith("float[]"))
        return true;
    else if (type.endsWith("char[]"))
        return true;
    else if (type.endsWith("short[]"))
        return true;
    else if (type.endsWith("double[]"))
        return true;
    else if (type.endsWith("boolean[]"))
        return true;
    return false;
}
