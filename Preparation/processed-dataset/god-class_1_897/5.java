private String substitute(String text) throws IOException {
    int startPos;
    if ((startPos = text.indexOf("${")) == -1) {
        return text;
    }
    // Find matching "}". 
    int braceDepth = 1;
    int endPos = startPos + 2;
    while (endPos < text.length() && braceDepth > 0) {
        if (text.charAt(endPos) == '{')
            braceDepth++;
        else if (text.charAt(endPos) == '}')
            braceDepth--;
        endPos++;
    }
    if (braceDepth != 0)
        throw new IOException("Mismatched \"{}\" in template string: " + text);
    final String variableExpression = text.substring(startPos + 2, endPos - 1);
    // Find the end of the variable name 
    String value = null;
    for (int i = 0; i < variableExpression.length(); i++) {
        char ch = variableExpression.charAt(i);
        if (ch == ':' && i < variableExpression.length() - 1 && variableExpression.charAt(i + 1) == '-') {
            value = substituteWithDefault(variableExpression.substring(0, i), variableExpression.substring(i + 2));
            break;
        } else if (ch == '?') {
            value = substituteWithConditional(variableExpression.substring(0, i), variableExpression.substring(i + 1));
            break;
        } else if (ch != '_' && !Character.isJavaIdentifierPart(ch)) {
            throw new IOException("Invalid variable in " + text);
        }
    }
    if (value == null) {
        value = substituteWithDefault(variableExpression, "");
    }
    return text.substring(0, startPos) + value + text.substring(endPos);
}
