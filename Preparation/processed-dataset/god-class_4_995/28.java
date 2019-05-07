/**
     *  Returns an element for the external link image (out.png).  However,
     *  this method caches the URL for the lifetime of this MarkupParser,
     *  because it's commonly used, and we'll end up with possibly hundreds
     *  our thousands of references to it...  It's a lot faster, too.
     *
     *  @return  An element containing the HTML for the outlink image.
     */
private Element outlinkImage() {
    Element el = null;
    if (m_useOutlinkImage) {
        if (m_outlinkImageURL == null) {
            m_outlinkImageURL = m_context.getURL(WikiContext.NONE, OUTLINK_IMAGE);
        }
        el = new Element("img").setAttribute("class", "outlink");
        el.setAttribute("src", m_outlinkImageURL);
        el.setAttribute("alt", "");
    }
    return el;
}
