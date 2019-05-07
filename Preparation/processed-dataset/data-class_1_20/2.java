String d(long scale) {
    long report = elapsed / scale;
    elapsed -= report * scale;
    return Long.toString(report);
}
