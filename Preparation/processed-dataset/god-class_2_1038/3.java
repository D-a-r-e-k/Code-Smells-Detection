public List<UrlPattern> getSortedUrlPatterns() {
    List<UrlPattern> urlList = new LinkedList<UrlPattern>();
    urlList.addAll(site.getUrlPatterns());
    Collections.sort(urlList, new UrlPattern.UrlComparator());
    return urlList;
}
