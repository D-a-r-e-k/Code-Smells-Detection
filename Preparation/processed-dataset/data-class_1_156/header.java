void method0() { 
public static final int KEY_BASE;
/**
     * Hint as to the transcoding destination.
     */
public static final RenderingHints.Key KEY_TRANSCODING;
public static final String VALUE_TRANSCODING_PRINTING = new String("Printing");
/**
     * Key for the AOI hint. This hint is used to propagate the AOI to Paint
     * and PaintContext instances.
     */
public static final RenderingHints.Key KEY_AREA_OF_INTEREST;
/**
     * Hint for the destination of the rendering when it is a BufferedImage
     * This works around the fact that Java 2D sometimes lies about the
     * attributes of the Graphics2D device, when it is an image.
     *
     * It is strongly suggested that you use
     * org.apache.batik.ext.awt.image.GraphicsUtil.createGraphics to
     * create a Graphics2D from a BufferedImage, this will ensure that
     * the proper things are done in the processes of creating the
     * Graphics.  */
public static final RenderingHints.Key KEY_BUFFERED_IMAGE;
/**
     * Hint to source that we only want an alpha channel.
     * The source should follow the SVG spec for how to
     * convert ARGB, RGB, Grey and AGrey to just an Alpha channel.
     */
public static final RenderingHints.Key KEY_COLORSPACE;
}
