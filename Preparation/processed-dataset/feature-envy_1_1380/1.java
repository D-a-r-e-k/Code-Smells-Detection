protected boolean isValidConnectString(String url) {
    return (url != null && url.startsWith(URL_PREFIX));
}
