protected Connection createConnection(String url) throws AxionException {
    String name = null;
    File path = null;
    String prefixStripped = url.substring(URL_PREFIX.length());
    int colon = prefixStripped.indexOf(":");
    if (colon == -1 || (prefixStripped.length() - 1 == colon)) {
        name = prefixStripped;
    } else {
        name = prefixStripped.substring(0, colon);
        path = new File(prefixStripped.substring(colon + 1));
    }
    return new AxionConnection(name, path, url);
}
