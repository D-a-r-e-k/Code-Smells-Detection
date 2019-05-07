/**
     *  Creates a JDOM anchor element.  Can be overridden to change the URL creation,
     *  if you really know what you are doing.
     *
     *  @param type One of the types above
     *  @param link URL to which to link to
     *  @param text Link text
     *  @param section If a particular section identifier is required.
     *  @return An A element.
     *  @since 2.4.78
     */
protected Element createAnchor(int type, String link, String text, String section) {
    text = escapeHTMLEntities(text);
    section = escapeHTMLEntities(section);
    Element el = new Element("a");
    el.setAttribute("class", CLASS_TYPES[type]);
    el.setAttribute("href", link + section);
    el.addContent(text);
    return el;
}
