// TODO: make static? 
protected String encodeSpaces(String path) {
    return JOrphanUtils.replaceAllChars(path, ' ', "%20");
}
