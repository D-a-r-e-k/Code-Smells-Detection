private String getUrl(Element element) {
    if (element.getName().equals("outline")) {
        String url = Tools.getAttribute(element, "xmlUrl");
        if (url != null)
            return url;
        url = Tools.getAttribute(element, "url");
        if (url != null)
            return url;
    }
    return null;
}
