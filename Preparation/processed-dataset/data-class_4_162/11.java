/**
     *  Matches the given link to the list of image name patterns
     *  to determine whether it should be treated as an inline image
     *  or not.
     */
private boolean isImageLink(String link) {
    if (m_inlineImages) {
        link = link.toLowerCase();
        for (Iterator i = m_inlineImagePatterns.iterator(); i.hasNext(); ) {
            if (m_inlineMatcher.matches(link, (Pattern) i.next()))
                return true;
        }
    }
    return false;
}
