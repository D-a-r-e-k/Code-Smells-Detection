/**
   * @param substring
   * @param defaultValue
   * @return
   * @throws IOException 
   */
private String substituteWithConditional(String variableName, String values) throws IOException {
    // Split values into true and false values. 
    int pos = values.indexOf(':');
    if (pos == -1)
        throw new IOException("No ':' separator in " + values);
    if (evaluate(variableName))
        return substitute(values.substring(0, pos));
    else
        return substitute(values.substring(pos + 1));
}
