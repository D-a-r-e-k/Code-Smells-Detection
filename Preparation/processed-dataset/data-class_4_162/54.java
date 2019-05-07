private Element handleBar(boolean newLine) throws IOException {
    Element el = null;
    if (!m_istable && !newLine) {
        return null;
    }
    // 
    //  If the bar is in the first column, we will either start 
    //  a new table or continue the old one. 
    // 
    if (newLine) {
        if (!m_istable) {
            startBlockLevel();
            el = pushElement(new Element("table").setAttribute("class", "wikitable").setAttribute("border", "1"));
            m_istable = true;
            m_rowNum = 0;
        }
        m_rowNum++;
        Element tr = (m_rowNum % 2 != 0) ? new Element("tr").setAttribute("class", "odd") : new Element("tr");
        el = pushElement(tr);
    }
    // 
    //  Check out which table cell element to start; 
    //  a header element (th) or a regular element (td). 
    // 
    int ch = nextToken();
    if (ch == '|') {
        if (!newLine) {
            el = popElement("th");
            if (el == null)
                popElement("td");
        }
        el = pushElement(new Element("th"));
    } else {
        if (!newLine) {
            el = popElement("td");
            if (el == null)
                popElement("th");
        }
        el = pushElement(new Element("td"));
        pushBack(ch);
    }
    return el;
}
