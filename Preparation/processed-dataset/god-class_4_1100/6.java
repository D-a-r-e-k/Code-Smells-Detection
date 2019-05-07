/**
     * This is the main reading method.
     *
     * @param in The input stream.
     * @param drawing The drawing to which this method adds figures.
     * @param replace Whether attributes on the drawing object
     * should by changed by this method. Set this to false, when reading individual
     * images from the clipboard.
     */
@Override
public void read(InputStream in, Drawing drawing, boolean replace) throws IOException {
    long start = System.currentTimeMillis();
    this.figures = new LinkedList<Figure>();
    IXMLParser parser;
    try {
        parser = XMLParserFactory.createDefaultXMLParser();
    } catch (Exception ex) {
        InternalError e = new InternalError("Unable to instantiate NanoXML Parser");
        e.initCause(ex);
        throw e;
    }
    System.out.println("SVGInputFormat parser created " + (System.currentTimeMillis() - start));
    IXMLReader reader = new StdXMLReader(in);
    parser.setReader(reader);
    System.out.println("SVGInputFormat reader created " + (System.currentTimeMillis() - start));
    try {
        document = (IXMLElement) parser.parse();
    } catch (XMLException ex) {
        IOException e = new IOException(ex.getMessage());
        e.initCause(ex);
        throw e;
    }
    System.out.println("SVGInputFormat document created " + (System.currentTimeMillis() - start));
    // Search for the first 'svg' element in the XML document 
    // in preorder sequence 
    IXMLElement svg = document;
    Stack<Iterator<IXMLElement>> stack = new Stack<Iterator<IXMLElement>>();
    LinkedList<IXMLElement> ll = new LinkedList<IXMLElement>();
    ll.add(document);
    stack.push(ll.iterator());
    while (!stack.empty() && stack.peek().hasNext()) {
        Iterator<IXMLElement> iter = stack.peek();
        IXMLElement node = iter.next();
        Iterator<IXMLElement> children = (node.getChildren() == null) ? null : node.getChildren().iterator();
        if (!iter.hasNext()) {
            stack.pop();
        }
        if (children != null && children.hasNext()) {
            stack.push(children);
        }
        if (node.getName() != null && node.getName().equals("svg") && (node.getNamespace() == null || node.getNamespace().equals(SVG_NAMESPACE))) {
            svg = node;
            break;
        }
    }
    if (svg.getName() == null || !svg.getName().equals("svg") || (svg.getNamespace() != null && !svg.getNamespace().equals(SVG_NAMESPACE))) {
        throw new IOException("'svg' element expected: " + svg.getName());
    }
    //long end1 = System.currentTimeMillis(); 
    // Flatten CSS Styles 
    initStorageContext(document);
    flattenStyles(svg);
    //long end2 = System.currentTimeMillis(); 
    readElement(svg);
    long end = System.currentTimeMillis();
    if (DEBUG) {
        System.out.println("SVGInputFormat elapsed:" + (end - start));
    }
    /*if (DEBUG) System.out.println("SVGInputFormat read:"+(end1-start));
        if (DEBUG) System.out.println("SVGInputFormat flatten:"+(end2-end1));
        if (DEBUG) System.out.println("SVGInputFormat build:"+(end-end2));
         */
    if (replace) {
        drawing.removeAllChildren();
    }
    drawing.addAll(figures);
    if (replace) {
        Viewport viewport = viewportStack.firstElement();
        drawing.set(VIEWPORT_FILL, VIEWPORT_FILL.get(viewport.attributes));
        drawing.set(VIEWPORT_FILL_OPACITY, VIEWPORT_FILL_OPACITY.get(viewport.attributes));
        drawing.set(VIEWPORT_HEIGHT, VIEWPORT_HEIGHT.get(viewport.attributes));
        drawing.set(VIEWPORT_WIDTH, VIEWPORT_WIDTH.get(viewport.attributes));
    }
    // Get rid of all objects we don't need anymore to help garbage collector. 
    document.dispose();
    identifiedElements.clear();
    elementObjects.clear();
    viewportStack.clear();
    styleManager.clear();
    document = null;
    identifiedElements = null;
    elementObjects = null;
    viewportStack = null;
    styleManager = null;
}
