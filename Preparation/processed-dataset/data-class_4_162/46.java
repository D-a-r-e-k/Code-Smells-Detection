private static String getListType(char c) {
    if (c == '*') {
        return "ul";
    } else if (c == '#') {
        return "ol";
    }
    throw new InternalWikiException("Parser got faulty list type: " + c);
}
