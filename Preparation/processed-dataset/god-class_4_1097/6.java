public void read(File file, Drawing drawing, boolean replace) throws IOException {
    BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
    try {
        read(in, drawing, replace);
    } finally {
        in.close();
    }
}
