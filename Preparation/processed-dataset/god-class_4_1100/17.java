/**
     * Reads an SVG "image" element.
     */
private Figure readImageElement(IXMLElement elem) throws IOException {
    HashMap<AttributeKey, Object> a = new HashMap<AttributeKey, Object>();
    readCoreAttributes(elem, a);
    readTransformAttribute(elem, a);
    readOpacityAttribute(elem, a);
    double x = toNumber(elem, readAttribute(elem, "x", "0"));
    double y = toNumber(elem, readAttribute(elem, "y", "0"));
    double w = toWidth(elem, readAttribute(elem, "width", "0"));
    double h = toHeight(elem, readAttribute(elem, "height", "0"));
    String href = readAttribute(elem, "xlink:href", null);
    if (href == null) {
        href = readAttribute(elem, "href", null);
    }
    byte[] imageData = null;
    if (href != null) {
        if (href.startsWith("data:")) {
            int semicolonPos = href.indexOf(';');
            if (semicolonPos != -1) {
                if (href.indexOf(";base64,") == semicolonPos) {
                    imageData = Base64.decode(href.substring(semicolonPos + 8));
                } else {
                    throw new IOException("Unsupported encoding in data href in image element:" + href);
                }
            } else {
                throw new IOException("Unsupported data href in image element:" + href);
            }
        } else {
            URL imageUrl = new URL(url, href);
            // Check whether the imageURL is an SVG image. 
            // Load it as a group. 
            if (imageUrl.getFile().endsWith("svg")) {
                SVGInputFormat svgImage = new SVGInputFormat(factory);
                Drawing svgDrawing = new DefaultDrawing();
                svgImage.read(imageUrl, svgDrawing, true);
                CompositeFigure svgImageGroup = factory.createG(a);
                for (Figure f : svgDrawing.getChildren()) {
                    svgImageGroup.add(f);
                }
                svgImageGroup.setBounds(new Point2D.Double(x, y), new Point2D.Double(x + w, y + h));
                return svgImageGroup;
            }
            // Read the image data from the URL into a byte array 
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] buf = new byte[512];
            int len = 0;
            try {
                InputStream in = imageUrl.openStream();
                try {
                    while ((len = in.read(buf)) > 0) {
                        bout.write(buf, 0, len);
                    }
                    imageData = bout.toByteArray();
                } finally {
                    in.close();
                }
            } catch (FileNotFoundException e) {
            }
        }
    }
    // Create a buffered image from the image data 
    BufferedImage bufferedImage = null;
    if (imageData != null) {
        try {
            bufferedImage = ImageIO.read(new ByteArrayInputStream(imageData));
        } catch (IIOException e) {
            System.err.println("SVGInputFormat warning: skipped unsupported image format.");
            e.printStackTrace();
        }
    }
    // Delete the image data in case of failure 
    if (bufferedImage == null) {
        imageData = null;
    }
    // Create a figure from the image data and the buffered image. 
    Figure figure = factory.createImage(x, y, w, h, imageData, bufferedImage, a);
    elementObjects.put(elem, figure);
    return figure;
}
