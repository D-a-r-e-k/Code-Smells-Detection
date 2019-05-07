@Override
public void read(Transferable t, Drawing drawing, boolean replace) throws UnsupportedFlavorException, IOException {
    InputStream in = (InputStream) t.getTransferData(new DataFlavor("image/svg+xml", "Image SVG"));
    try {
        read(in, drawing, false);
    } finally {
        in.close();
    }
}
