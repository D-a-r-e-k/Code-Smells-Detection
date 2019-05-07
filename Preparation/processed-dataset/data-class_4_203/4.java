private boolean isFolder(Element element) {
    if (element.getName().equals("outline") || element.getName().equals("body")) // OPML 
    {
        String type = Tools.getAttribute(element, "type");
        if (type == null)
            return true;
        else if (type.equals("link"))
            return false;
        else if (type.equals("rss"))
            return false;
        return true;
    }
    return false;
}
