/**
     *  Image links are handled differently:
     *  1. If the text is a WikiName of an existing page,
     *     it gets linked.
     *  2. If the text is an external link, then it is inlined.
     *  3. Otherwise it becomes an ALT text.
     *
     *  @param reallink The link to the image.
     *  @param link     Link text portion, may be a link to somewhere else.
     *  @param hasLinkText If true, then the defined link had a link text available.
     *                  This means that the link text may be a link to a wiki page,
     *                  or an external resource.
     */
// FIXME: isExternalLink() is called twice. 
private Element handleImageLink(String reallink, String link, boolean hasLinkText) {
    String possiblePage = MarkupParser.cleanLink(link);
    if (isExternalLink(link) && hasLinkText) {
        return makeLink(IMAGELINK, reallink, link, null, null);
    } else if ((linkExists(possiblePage)) != null && hasLinkText) {
        // System.out.println("Orig="+link+", Matched: "+matchedLink); 
        callMutatorChain(m_localLinkMutatorChain, possiblePage);
        return makeLink(IMAGEWIKILINK, reallink, link, null, null);
    } else {
        return makeLink(IMAGE, reallink, link, null, null);
    }
}
