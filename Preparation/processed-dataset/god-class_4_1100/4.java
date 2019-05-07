public void read(File file, Drawing drawing, boolean replace) throws IOException {
    this.url = file.toURI().toURL();
    BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
    try {
        read(in, drawing, replace);
    } finally {
        in.close();
    }
    this.url = null;
}
