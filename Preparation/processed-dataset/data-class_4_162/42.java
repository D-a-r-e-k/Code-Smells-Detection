private Element handleDash() throws IOException {
    int ch = nextToken();
    if (ch == '-') {
        int ch2 = nextToken();
        if (ch2 == '-') {
            int ch3 = nextToken();
            if (ch3 == '-') {
                // Empty away all the rest of the dashes. 
                // Do not forget to return the first non-match back. 
                while ((ch = nextToken()) == '-') ;
                pushBack(ch);
                startBlockLevel();
                pushElement(new Element("hr"));
                return popElement("hr");
            }
            pushBack(ch3);
        }
        pushBack(ch2);
    }
    pushBack(ch);
    return null;
}
