/**
   * @param variableName
   * @param defaultValue
   * @return
   */
private String substituteWithDefault(String variableName, String defaultValue) throws IOException {
    Object obj = options.get(variableName.trim());
    if (obj == null || obj.toString().length() == 0)
        return substitute(defaultValue);
    return obj.toString();
}
