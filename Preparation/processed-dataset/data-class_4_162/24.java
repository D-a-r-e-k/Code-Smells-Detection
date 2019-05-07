/**
     *  Modifies the "hd" parameter to contain proper values.  Because
     *  an "id" tag may only contain [a-zA-Z0-9:_-], we'll replace the
     *  % after url encoding with '_'.
     *  <p>
     *  Counts also duplicate headings (= headings with similar name), and
     *  attaches a counter.
     */
private String makeHeadingAnchor(String baseName, String title, Heading hd) {
    hd.m_titleText = title;
    title = MarkupParser.wikifyLink(title);
    hd.m_titleSection = m_engine.encodeName(title);
    if (m_titleSectionCounter.containsKey(hd.m_titleSection)) {
        Integer count = m_titleSectionCounter.get(hd.m_titleSection);
        count = count + 1;
        m_titleSectionCounter.put(hd.m_titleSection, count);
        hd.m_titleSection += "-" + count;
    } else {
        m_titleSectionCounter.put(hd.m_titleSection, 1);
    }
    hd.m_titleAnchor = "section-" + m_engine.encodeName(baseName) + "-" + hd.m_titleSection;
    hd.m_titleAnchor = hd.m_titleAnchor.replace('%', '_');
    hd.m_titleAnchor = hd.m_titleAnchor.replace('/', '_');
    return hd.m_titleAnchor;
}
