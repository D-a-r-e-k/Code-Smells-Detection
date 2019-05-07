private String getUrl(Element element) {
    if (element.getName().equals("outline")) {
        return Tools.getAttribute(element, "url");
    }
    return null;
}
