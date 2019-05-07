void method0() { 
/**
     * Set this to true, to getChild debug output on if (DEBUG) System.out.
     */
private static final boolean DEBUG = false;
/**
     * The SVGFigure factory is used to create Figure's for the drawing.
     */
private SVGFigureFactory factory;
/**
     * URL pointing to the SVG input file. This is used as a base URL for
     * resources that are referenced from the SVG file.
     */
private URL url;
// FIXME - Move these maps to SVGConstants or to SVGAttributeKeys. 
/**
     * Maps to all XML elements that are identified by an xml:id.
     */
private HashMap<String, IXMLElement> identifiedElements;
/**
     * Maps to all drawing objects from the XML elements they were created from.
     */
private HashMap<IXMLElement, Object> elementObjects;
/**
     * Tokenizer for parsing SVG path expressions.
     *
     */
private StreamPosTokenizer toPathTokenizer;
/** FontFormatter for parsing font family names. */
private FontFormatter fontFormatter = new FontFormatter();
/**
     * Each SVG element creates a new Viewport that we store
     * here.
     */
private Stack<Viewport> viewportStack;
/**
     * Holds the style manager used for applying cascading style sheet CSS rules
     * to the document.
     */
private StyleManager styleManager;
/**
     * Holds the figures that are currently being read.
     */
private LinkedList<Figure> figures;
/**
     * Holds the document that is currently being read.
     */
private IXMLElement document;
private static final HashSet<String> supportedFeatures = new HashSet<String>(Arrays.asList(new String[] { "http://www.w3.org/Graphics/SVG/feature/1.2/#SVG-static", //"http://www.w3.org/Graphics/SVG/feature/1.2/#SVG-static-DOM", 
//"http://www.w3.org/Graphics/SVG/feature/1.2/#SVG-animated", 
//"http://www.w3.org/Graphics/SVG/feature/1.2/#SVG-all", 
"http://www.w3.org/Graphics/SVG/feature/1.2/#CoreAttribute", //"http://www.w3.org/Graphics/SVG/feature/1.2/#NavigationAttribute", 
"http://www.w3.org/Graphics/SVG/feature/1.2/#Structure", "http://www.w3.org/Graphics/SVG/feature/1.2/#ConditionalProcessing", "http://www.w3.org/Graphics/SVG/feature/1.2/#ConditionalProcessingAttribute", "http://www.w3.org/Graphics/SVG/feature/1.2/#Image", //"http://www.w3.org/Graphics/SVG/feature/1.2/#Prefetch", 
//"http://www.w3.org/Graphics/SVG/feature/1.2/#Discard", 
"http://www.w3.org/Graphics/SVG/feature/1.2/#Shape", "http://www.w3.org/Graphics/SVG/feature/1.2/#Text", "http://www.w3.org/Graphics/SVG/feature/1.2/#PaintAttribute", "http://www.w3.org/Graphics/SVG/feature/1.2/#OpacityAttribute", "http://www.w3.org/Graphics/SVG/feature/1.2/#GraphicsAttribute", "http://www.w3.org/Graphics/SVG/feature/1.2/#Gradient", "http://www.w3.org/Graphics/SVG/feature/1.2/#SolidColor", // 
"http://www.w3.org/Graphics/SVG/feature/1.2/#Hyperlinking" }));
}
