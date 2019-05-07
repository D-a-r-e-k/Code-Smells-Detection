/**
     * Reads a &lt;draw:frame&gt; element from the specified
     * XML element.
     * <p>
     * A frame is a rectangular container where that contains enhanced content
     * like text boxes, images or objects. Frames are very similar to regular
     * drawing shapes, but support some features that are not available for
     * regular drawing shapes, like contours, image maps and hyperlinks. In
     * particular, a frame allows to have multiple renditions of an object. That
     * is, a frame may for instance contain an object as well as an image. In
     * this case, the application may choose the content that it supports best.
     * If the application supports the object type contained in the frame, it
     * probably will render the object. If it does not support the object, it
     * will render the image.
     * <p>
     * In general, an application must not render more than one of the content
     * elements contained in a frame. The order of content elements dictates
     * the document author's preference for rendering, with the first child
     * being the most preferred. This means that applications should render the
     * first child element that it supports. A frame must contain at least one
     * content element. The inclusion of multiple content elements is optional.
     * Application may preserve the content elements they don't render, but
     * don't have to.
     * <p>
     * Within text documents, frames are also used to position content outside
     * the default text flow of a document.
     * <p>
     * Frames can contain:
     * • Text boxes
     * • Objects represented either in the OpenDocument format or in a object
     *      specific binary format
     * • Images
     * • Applets
     * • Plug-ins
     * • Floating frames
     * <p>
     * Like the formatting properties of drawing shapes, frame formatting
     * properties are stored in styles belonging to the graphic family. The way
     * a frame is contained in a document also is the same as for drawing shapes.
     *
     *
     * @param elem A &lt;frame&gt; element.
     */
private ODGFigure readFrameElement(IXMLElement elem) throws IOException {
    if (DEBUG) {
        System.out.println("ODGInputFormat.readFrameElement(" + elem + ") not implemented.");
    }
    return null;
}
