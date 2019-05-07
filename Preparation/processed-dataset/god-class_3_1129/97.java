protected static String encodeBackSlashes(String value) {
    StringBuilder newValue = new StringBuilder();
    for (int i = 0; i < value.length(); i++) {
        char charAt = value.charAt(i);
        if (charAt == '\\') {
            // $NON-NLS-1$ 
            newValue.append("\\\\");
        } else {
            newValue.append(charAt);
        }
    }
    return newValue.toString();
}
