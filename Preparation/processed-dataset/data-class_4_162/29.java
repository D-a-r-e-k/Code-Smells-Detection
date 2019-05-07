/**
     *  Takes an URL and turns it into a regular wiki link.  Unfortunately,
     *  because of the way that flushPlainText() works, it already encodes
     *  all of the XML entities.  But so does WikiContext.getURL(), so we
     *  have to do a reverse-replace here, so that it can again be replaced in makeLink.
     *  <p>
     *  What a crappy problem.
     *
     * @param url
     * @return An anchor Element containing the link.
     */
private Element makeDirectURILink(String url) {
    Element result;
    String last = null;
    if (url.endsWith(",") || url.endsWith(".")) {
        last = url.substring(url.length() - 1);
        url = url.substring(0, url.length() - 1);
    }
    callMutatorChain(m_externalLinkMutatorChain, url);
    if (isImageLink(url)) {
        result = handleImageLink(StringUtils.replace(url, "&amp;", "&"), url, false);
    } else {
        result = makeLink(EXTERNAL, StringUtils.replace(url, "&amp;", "&"), url, null, null);
        addElement(outlinkImage());
    }
    if (last != null) {
        m_plainTextBuf.append(last);
    }
    return result;
}
