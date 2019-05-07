/**
     * Reduces the buffer to the content between start and end marker when
     * only whitespaces are found before the startMarker as well as after the end marker
     */
static void reduceToContent(final XMLStringBuffer buffer, final String startMarker, final String endMarker) {
    int i = 0;
    int startContent = -1;
    final int l1 = startMarker.length();
    final int l2 = endMarker.length();
    while (i < buffer.length - l1 - l2) {
        final char c = buffer.ch[buffer.offset + i];
        if (Character.isWhitespace(c)) {
            ++i;
        } else if (c == startMarker.charAt(0) && startMarker.equals(new String(buffer.ch, buffer.offset + i, l1))) {
            startContent = buffer.offset + i + l1;
            break;
        } else {
            return;
        }
    }
    if (startContent == -1) {
        // start marker not found 
        return;
    }
    i = buffer.length - 1;
    while (i > startContent + l2) {
        final char c = buffer.ch[buffer.offset + i];
        if (Character.isWhitespace(c)) {
            --i;
        } else if (c == endMarker.charAt(l2 - 1) && endMarker.equals(new String(buffer.ch, buffer.offset + i - l2 + 1, l2))) {
            buffer.length = buffer.offset + i - startContent - 2;
            buffer.offset = startContent;
            return;
        } else {
            return;
        }
    }
}
