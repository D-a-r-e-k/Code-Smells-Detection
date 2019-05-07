/**
     *  Pushes back any string that has been read.  It will obviously
     *  be pushed back in a reverse order.
     *
     *  @since 2.1.77
     */
private void pushBack(String s) throws IOException {
    for (int i = s.length() - 1; i >= 0; i--) {
        pushBack(s.charAt(i));
    }
}
