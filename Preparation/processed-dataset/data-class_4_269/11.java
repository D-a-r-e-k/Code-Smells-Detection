/**
     * Reads figures from the content.xml file of an ODG open document drawing
     * document.
     */
@SuppressWarnings("unchecked")
public void readFiguresFromDocumentContent(InputStream in, Drawing drawing, boolean replace) throws IOException {
    this.figures = new LinkedList<Figure>();
    IXMLParser parser;
    try {
        parser = XMLParserFactory.createDefaultXMLParser();
    } catch (Exception ex) {
        InternalError e = new InternalError("Unable to instantiate NanoXML Parser");
        e.initCause(ex);
        throw e;
    }
    IXMLReader reader = new StdXMLReader(in);
    parser.setReader(reader);
    try {
        document = (IXMLElement) parser.parse();
    } catch (XMLException ex) {
        IOException e = new IOException(ex.getMessage());
        e.initCause(ex);
        throw e;
    }
    if (styles == null) {
        styles = new ODGStylesReader();
    }
    styles.read(document);
    // Search for the first 'office:drawing' element in the XML document 
    // in preorder sequence 
    IXMLElement drawingElem = document;
    Stack<Iterator> stack = new Stack<Iterator>();
    LinkedList<IXMLElement> ll = new LinkedList<IXMLElement>();
    ll.add(document);
    stack.push(ll.iterator());
    while (!stack.empty() && stack.peek().hasNext()) {
        Iterator<IXMLElement> iter = stack.peek();
        IXMLElement node = iter.next();
        Iterator<IXMLElement> children = node.getChildren().iterator();
        if (!iter.hasNext()) {
            stack.pop();
        }
        if (children.hasNext()) {
            stack.push(children);
        }
        if (node.getName() != null && node.getName().equals("drawing") && (node.getNamespace() == null || node.getNamespace().equals(OFFICE_NAMESPACE))) {
            drawingElem = node;
            break;
        }
    }
    if (drawingElem.getName() == null || !drawingElem.getName().equals("drawing") || (drawingElem.getNamespace() != null && !drawingElem.getNamespace().equals(OFFICE_NAMESPACE))) {
        throw new IOException("'office:drawing' element expected: " + drawingElem.getName());
    }
    readDrawingElement(drawingElem);
    if (replace) {
        drawing.removeAllChildren();
    }
    drawing.addAll(figures);
}
