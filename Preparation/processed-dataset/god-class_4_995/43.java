private Element handleHeading() throws IOException {
    Element el = null;
    int ch = nextToken();
    Heading hd = new Heading();
    if (ch == '!') {
        int ch2 = nextToken();
        if (ch2 == '!') {
            String title = peekAheadLine();
            el = makeHeading(Heading.HEADING_LARGE, title, hd);
        } else {
            pushBack(ch2);
            String title = peekAheadLine();
            el = makeHeading(Heading.HEADING_MEDIUM, title, hd);
        }
    } else {
        pushBack(ch);
        String title = peekAheadLine();
        el = makeHeading(Heading.HEADING_SMALL, title, hd);
    }
    callHeadingListenerChain(hd);
    m_lastHeading = hd;
    if (el != null)
        pushElement(el);
    return el;
}
