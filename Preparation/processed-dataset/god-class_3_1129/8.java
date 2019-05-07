public String getPath() {
    String p = getPropertyAsString(PATH);
    return encodeSpaces(p);
}
