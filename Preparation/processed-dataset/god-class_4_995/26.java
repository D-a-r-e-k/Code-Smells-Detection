/**
     *  Returns XHTML for the heading. 
     *  
     *  @param level The level of the heading.  @see Heading
     *  @param title the title for the heading
     *  @param hd a List to which heading should be added
     *  @return An Element containing the heading
     */
public Element makeHeading(int level, String title, Heading hd) {
    Element el = null;
    String pageName = m_context.getPage().getName();
    String outTitle = makeSectionTitle(title);
    hd.m_level = level;
    switch(level) {
        case Heading.HEADING_SMALL:
            el = new Element("h4").setAttribute("id", makeHeadingAnchor(pageName, outTitle, hd));
            break;
        case Heading.HEADING_MEDIUM:
            el = new Element("h3").setAttribute("id", makeHeadingAnchor(pageName, outTitle, hd));
            break;
        case Heading.HEADING_LARGE:
            el = new Element("h2").setAttribute("id", makeHeadingAnchor(pageName, outTitle, hd));
            break;
        default:
            throw new InternalWikiException("Illegal heading type " + level);
    }
    return el;
}
