public boolean checkCondition(WebResponse response, WebTable block, ExoWebClient client) throws Exception {
    WebLink link = Util.findLinkWithURL(response, block, partOfURL_);
    return link != null;
}
