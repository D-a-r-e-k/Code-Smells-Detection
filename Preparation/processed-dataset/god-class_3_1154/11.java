private static String readWord(PushbackReader reader) throws IOException {
    StringBuilder sb = new StringBuilder();
    int ch;
    while ((ch = read(reader)) != -1) {
        if (Character.isLetterOrDigit(ch) || ch == '_' || ch == '.' || ch == '$') {
            sb.append((char) ch);
        } else {
            reader.unread(ch);
            break;
        }
    }
    return sb.toString();
}
