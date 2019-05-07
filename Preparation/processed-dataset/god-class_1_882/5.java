private static String javaNormalize(String text) {
    StringBuffer buf = new StringBuffer(text.length());
    if (Character.isJavaIdentifierStart(text.charAt(0))) {
        buf.append(text.charAt(0));
    } else {
        buf.append('_');
    }
    for (int i = 1; i < text.length(); ++i) {
        if (Character.isLetterOrDigit(text.charAt(i))) {
            buf.append(text.charAt(i));
        } else {
            buf.append('_');
        }
    }
    String ret = buf.toString();
    return ret;
}
