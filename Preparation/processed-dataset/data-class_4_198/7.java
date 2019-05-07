public static String cleanSpaces(String value) {
    StringBuffer buffer = new StringBuffer();
    synchronized (buffer) {
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) != ' ')
                buffer.append(value.charAt(i));
        }
    }
    return buffer.toString().toLowerCase();
}
