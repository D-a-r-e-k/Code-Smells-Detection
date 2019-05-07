private void closeHeadings() {
    if (m_lastHeading != null && !m_wysiwygEditorMode) {
        // Add the hash anchor element at the end of the heading 
        addElement(new Element("a").setAttribute("class", "hashlink").setAttribute("href", "#" + m_lastHeading.m_titleAnchor).setText("#"));
        m_lastHeading = null;
    }
    popElement("h2");
    popElement("h3");
    popElement("h4");
}
