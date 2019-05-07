private Element addElement(Content e) {
    if (e != null) {
        flushPlainText();
        m_currentElement.addContent(e);
    }
    return m_currentElement;
}
