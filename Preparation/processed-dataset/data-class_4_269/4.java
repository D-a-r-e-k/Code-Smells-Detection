@Override
public void read(URI uri, Drawing drawing, boolean replace) throws IOException {
    read(new File(uri), drawing, replace);
}
