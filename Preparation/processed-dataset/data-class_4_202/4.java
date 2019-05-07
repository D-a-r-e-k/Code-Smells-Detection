private boolean isFolder(Element element) {
    if (element.getName().equals("outline") || element.getName().equals("body")) // OPML 
    {
        String type = Tools.getAttribute(element, "type");
        String xmlUrl = Tools.getAttribute(element, "xmlUrl");
        if (xmlUrl != null)
            return false;
        else if (type == null)
            return true;
        else if (type.equalsIgnoreCase("link"))
            return false;
        else if (type.equalsIgnoreCase("rss"))
            return false;
        return true;
    }
    return false;
}
