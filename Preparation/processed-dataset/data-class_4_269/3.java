@Override
public void read(URI uri, Drawing drawing) throws IOException {
    read(new File(uri), drawing);
}
