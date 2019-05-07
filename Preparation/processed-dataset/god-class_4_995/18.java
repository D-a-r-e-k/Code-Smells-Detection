private Element pushElement(Element e) {
    flushPlainText();
    m_currentElement.addContent(e);
    m_currentElement = e;
    return e;
}
