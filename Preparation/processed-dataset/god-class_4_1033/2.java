void reply(HttpRequest httprequest, int i, String s) throws IOException {
    log.debug(httprequest.getInetAddress().getHostAddress() + " " + httprequest.getURI() + " HTTP " + httprequest.getMethod() + " - " + i + " - " + s);
    httprequest.reply(i, s);
}
