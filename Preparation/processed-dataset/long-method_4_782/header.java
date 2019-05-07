void method0() { 
/**
     * Logger from commons-logging.
     */
private static final Log LOG = LogFactory.getLog(UnmarshalHandler.class);
//---------------------------/  
//- Private Class Variables -/  
//---------------------------/  
/**
     * The error message when no class descriptor has been found
     * TODO: move to resource bundle
     */
private static final String ERROR_DID_NOT_FIND_CLASSDESCRIPTOR = "unable to find or create a ClassDescriptor for class: ";
/**
     * The built-in XML prefix used for xml:space, xml:lang
     * and, as the XML 1.0 Namespaces document specifies, are
     * reserved for use by XML and XML related specs.
    **/
private static final String XML_PREFIX = "xml";
/**
     * Attribute name for default namespace declaration
    **/
private static final String XMLNS = "xmlns";
/**
     * Attribute prefix for prefixed namespace declaration
    **/
private static final String XMLNS_PREFIX = "xmlns:";
private static final int XMLNS_PREFIX_LENGTH = XMLNS_PREFIX.length();
/**
     * The type attribute (xsi:type) used to denote the
     * XML Schema type of the parent element
    **/
private static final String XSI_TYPE = "type";
private static final String XML_SPACE = "space";
private static final String XML_SPACE_WITH_PREFIX = "xml:space";
private static final String PRESERVE = "preserve";
//----------------------------/  
//- Private Member Variables -/  
//----------------------------/  
private Stack _stateInfo = null;
private UnmarshalState _topState = null;
private Class _topClass = null;
/**
     * The top-level instance object, this may be set by the user
     * by calling #setRootObject();.
    **/
private Object _topObject = null;
/**
     * Indicates whether or not collections should be cleared
     * upon first use (to remove default values, or old values).
     * False by default for backward compatibility.
     */
private boolean _clearCollections = false;
/**
     * The SAX Document Locator.
    **/
private Locator _locator = null;
/**
     * The IDResolver for resolving IDReferences.
    **/
private IDResolver _idResolver = null;
/**
    * The unmarshaller listener.
    */
private org.castor.xml.UnmarshalListener _unmarshalListener = null;
/**
     * A flag indicating whether or not to perform validation.
     **/
private boolean _validate = true;
private Hashtable _resolveTable = new Hashtable();
private Map _javaPackages = null;
private ClassLoader _loader = null;
private static final StringClassDescriptor STRING_DESCRIPTOR = new StringClassDescriptor();
/**
     * A SAX2ANY unmarshaller in case we are dealing with {@literal <any>}.
     */
private SAX2ANY _anyUnmarshaller = null;
/**
     * The any branch depth.
     */
private int _depth = 0;
/**
     * The AnyNode to add (if any).
     */
private org.exolab.castor.types.AnyNode _node = null;
/**
     * The namespace stack.
     */
private Namespaces _namespaces = null;
/**
     * A map of namespace URIs to Package Names.
     */
private HashMap _namespaceToPackage = null;
/**
     * A reference to the ObjectFactory used to create instances
     * of the classes if the FieldHandler is not used.
     */
private ObjectFactory _objectFactory = new DefaultObjectFactory();
/**
     * A boolean to indicate that objects should
     * be re-used where appropriate.
    **/
private boolean _reuseObjects = false;
/**
     * A boolean that indicates attribute processing should
     * be strict and an error should be flagged if any
     * extra attributes exist.
    **/
private boolean _strictAttributes = false;
/**
     * A boolean that indicates element processing should
     * be strict and an error should be flagged if any
     * extra elements exist.
    **/
private boolean _strictElements = true;
/**
     * A depth counter that increases as we skip elements ( in startElement )
     * and decreases as we process and endElement. Only active if _strictElemnts
     */
private int _ignoreElementDepth = 0;
/**
     * A flag to keep track of when a new namespace scope is needed.
     */
private boolean _createNamespaceScope = true;
/**
     * Keeps track of the current element information
     * as passed by the parser.
     */
private ElementInfo _elemInfo = null;
/**
     * A "reusable" AttributeSet, for use when handling
     * SAX 2 ContentHandler.
     */
private AttributeSetImpl _reusableAtts = null;
/**
     * The top-level xml:space value.
     */
private boolean _wsPreserve = false;
}
