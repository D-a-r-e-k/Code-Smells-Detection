private void setWikiContext(WikiContext wikiContext) {
    if (wikiContext.getPage() != null) {
        setPageName(wikiContext.getPage().getName() + '/');
    }
}
