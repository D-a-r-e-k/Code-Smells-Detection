@Override
public void read(InputStream in, Drawing drawing, boolean replace) throws IOException {
    // Read the file into a byte array. 
    byte[] tmp = readAllBytes(in);
    // Input stream of the content.xml file 
    InputStream contentIn = null;
    // Input stream of the styles.xml file 
    InputStream stylesIn = null;
    // Try to read "tmp" as a ZIP-File. 
    boolean isZipped = true;
    try {
        ZipInputStream zin = new ZipInputStream(new ByteArrayInputStream(tmp));
        for (ZipEntry entry; null != (entry = zin.getNextEntry()); ) {
            if (entry.getName().equals("content.xml")) {
                contentIn = new ByteArrayInputStream(readAllBytes(zin));
            } else if (entry.getName().equals("styles.xml")) {
                stylesIn = new ByteArrayInputStream(readAllBytes(zin));
            }
        }
    } catch (ZipException e) {
        isZipped = false;
    }
    if (contentIn == null) {
        contentIn = new ByteArrayInputStream(tmp);
    }
    if (stylesIn == null) {
        stylesIn = new ByteArrayInputStream(tmp);
    }
    styles = new ODGStylesReader();
    styles.read(stylesIn);
    readFiguresFromDocumentContent(contentIn, drawing, replace);
}
