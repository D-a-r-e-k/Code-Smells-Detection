/**
     * Gets an instance of a Image from a java.awt.Image.
     * The image is added as a JPEG with a user defined quality.
     *
     * @param cb
     *            the <CODE>PdfContentByte</CODE> object to which the image will be added
     * @param awtImage
     *            the <CODE>java.awt.Image</CODE> to convert
     * @param quality
     *            a float value between 0 and 1
     * @return an object of type <CODE>PdfTemplate</CODE>
     * @throws BadElementException
     *             on error
     * @throws IOException
     */
public static Image getInstance(PdfContentByte cb, java.awt.Image awtImage, float quality) throws BadElementException, IOException {
    java.awt.image.PixelGrabber pg = new java.awt.image.PixelGrabber(awtImage, 0, 0, -1, -1, true);
    try {
        pg.grabPixels();
    } catch (InterruptedException e) {
        throw new IOException(MessageLocalization.getComposedMessage("java.awt.image.interrupted.waiting.for.pixels"));
    }
    if ((pg.getStatus() & java.awt.image.ImageObserver.ABORT) != 0) {
        throw new IOException(MessageLocalization.getComposedMessage("java.awt.image.fetch.aborted.or.errored"));
    }
    int w = pg.getWidth();
    int h = pg.getHeight();
    PdfTemplate tp = cb.createTemplate(w, h);
    Graphics2D g2d = tp.createGraphics(w, h, true, quality);
    g2d.drawImage(awtImage, 0, 0, null);
    g2d.dispose();
    return getInstance(tp);
}
