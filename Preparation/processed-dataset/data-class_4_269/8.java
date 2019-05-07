@Override
public void read(Transferable t, Drawing drawing, boolean replace) throws UnsupportedFlavorException, IOException {
    InputStream in = (InputStream) t.getTransferData(new DataFlavor("application/vnd.oasis.opendocument.graphics", "Image SVG"));
    try {
        read(in, drawing, replace);
    } finally {
        in.close();
    }
}
