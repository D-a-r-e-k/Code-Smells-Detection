public static boolean isValueFunction(int tokenType) {
    return customValueFuncMap.get(tokenType, -1) != -1;
}
