public String getContentType() {
    if (content_type != null) {
        return content_type;
    } else {
        content_type = WebMailServer.getStorage().getMimeType(name);
        return content_type;
    }
}
