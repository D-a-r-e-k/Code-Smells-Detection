private boolean evaluate(String condition) {
    condition = condition.trim();
    Object obj = options.get(condition);
    if (obj == null) {
        return condition.equalsIgnoreCase("true") || condition.equalsIgnoreCase("yes");
    }
    if (obj instanceof Boolean) {
        return ((Boolean) obj).booleanValue();
    } else if (obj instanceof String) {
        String string = ((String) obj).trim();
        return string.length() > 0 && !string.equalsIgnoreCase("false") && !string.equalsIgnoreCase("no");
    }
    return false;
}
