private Element makeLink(int type, String link, String text, String section, Iterator attributes) {
    Element el = null;
    if (text == null)
        text = link;
    text = callMutatorChain(m_linkMutators, text);
    section = (section != null) ? ("#" + section) : "";
    // Make sure we make a link name that can be accepted 
    // as a valid URL. 
    if (link.length() == 0) {
        type = EMPTY;
    }
    ResourceBundle rb = m_context.getBundle(InternationalizationManager.CORE_BUNDLE);
    Object[] args = { link };
    switch(type) {
        case READ:
            el = createAnchor(READ, m_context.getURL(WikiContext.VIEW, link), text, section);
            break;
        case EDIT:
            el = createAnchor(EDIT, m_context.getURL(WikiContext.EDIT, link), text, "");
            el.setAttribute("title", MessageFormat.format(rb.getString("markupparser.link.create"), args));
            break;
        case EMPTY:
            el = new Element("u").addContent(text);
            break;
        // 
        //  These two are for local references - footnotes and 
        //  references to footnotes. 
        //  We embed the page name (or whatever WikiContext gives us) 
        //  to make sure the links are unique across Wiki. 
        // 
        case LOCALREF:
            el = createAnchor(LOCALREF, "#ref-" + m_context.getName() + "-" + link, "[" + text + "]", "");
            break;
        case LOCAL:
            el = new Element("a").setAttribute("class", "footnote");
            el.setAttribute("name", "ref-" + m_context.getName() + "-" + link.substring(1));
            el.addContent("[" + text + "]");
            break;
        // 
        //  With the image, external and interwiki types we need to 
        //  make sure nobody can put in Javascript or something else 
        //  annoying into the links themselves.  We do this by preventing 
        //  a haxor from stopping the link name short with quotes in 
        //  fillBuffer(). 
        // 
        case IMAGE:
            el = new Element("img").setAttribute("class", "inline");
            el.setAttribute("src", link);
            el.setAttribute("alt", text);
            break;
        case IMAGELINK:
            el = new Element("img").setAttribute("class", "inline");
            el.setAttribute("src", link);
            el.setAttribute("alt", text);
            el = createAnchor(IMAGELINK, text, "", "").addContent(el);
            break;
        case IMAGEWIKILINK:
            String pagelink = m_context.getURL(WikiContext.VIEW, text);
            el = new Element("img").setAttribute("class", "inline");
            el.setAttribute("src", link);
            el.setAttribute("alt", text);
            el = createAnchor(IMAGEWIKILINK, pagelink, "", "").addContent(el);
            break;
        case EXTERNAL:
            el = createAnchor(EXTERNAL, link, text, section);
            if (m_useRelNofollow)
                el.setAttribute("rel", "nofollow");
            break;
        case INTERWIKI:
            el = createAnchor(INTERWIKI, link, text, section);
            break;
        case ATTACHMENT:
            String attlink = m_context.getURL(WikiContext.ATTACH, link);
            String infolink = m_context.getURL(WikiContext.INFO, link);
            String imglink = m_context.getURL(WikiContext.NONE, "images/attachment_small.png");
            el = createAnchor(ATTACHMENT, attlink, text, "");
            pushElement(el);
            popElement(el.getName());
            if (m_useAttachmentImage) {
                el = new Element("img").setAttribute("src", imglink);
                el.setAttribute("border", "0");
                el.setAttribute("alt", "(info)");
                el = new Element("a").setAttribute("href", infolink).addContent(el);
                el.setAttribute("class", "infolink");
            } else {
                el = null;
            }
            break;
        default:
            break;
    }
    if (el != null && attributes != null) {
        while (attributes.hasNext()) {
            Attribute attr = (Attribute) attributes.next();
            if (attr != null) {
                el.setAttribute(attr);
            }
        }
    }
    if (el != null) {
        flushPlainText();
        m_currentElement.addContent(el);
    }
    return el;
}
