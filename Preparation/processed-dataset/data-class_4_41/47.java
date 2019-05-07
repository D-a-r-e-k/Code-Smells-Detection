private boolean endsWith(final XMLStringBuffer buffer, final String string) {
    final int l = string.length();
    if (buffer.length < l) {
        return false;
    } else {
        final String s = new String(buffer.ch, buffer.length - l, l);
        return string.equals(s);
    }
}
