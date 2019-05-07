public static boolean isRegularFunction(int tokenType) {
    return customRegularFuncMap.get(tokenType, -1) != -1;
}
