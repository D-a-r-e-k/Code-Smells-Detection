private Element handleBackslash() throws IOException {
    int ch = nextToken();
    if (ch == '\\') {
        int ch2 = nextToken();
        if (ch2 == '\\') {
            pushElement(new Element("br").setAttribute("clear", "all"));
            return popElement("br");
        }
        pushBack(ch2);
        pushElement(new Element("br"));
        return popElement("br");
    }
    pushBack(ch);
    return null;
}
