public void read(URL url, Drawing drawing, boolean replace) throws IOException {
    this.url = url;
    InputStream in = url.openStream();
    try {
        read(in, drawing, replace);
    } finally {
        in.close();
    }
    this.url = null;
}
