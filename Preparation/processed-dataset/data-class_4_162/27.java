/**
     *  When given a link to a WikiName, we just return
     *  a proper HTML link for it.  The local link mutator
     *  chain is also called.
     */
private Element makeCamelCaseLink(String wikiname) {
    String matchedLink;
    callMutatorChain(m_localLinkMutatorChain, wikiname);
    if ((matchedLink = linkExists(wikiname)) != null) {
        makeLink(READ, matchedLink, wikiname, null, null);
    } else {
        makeLink(EDIT, wikiname, wikiname, null, null);
    }
    return m_currentElement;
}
