void method0() { 
private static final boolean DEBUG = false;
/**
     * Most office applications support styles within their user interface.
     * Within this specification, the XML representations of such styles are
     * referred to as styles. When a differentiation from the other types of
     * styles is required, they are referred to as common styles.
     * The term common indicates that this is the type of style that an office
     * application user considers to be a style.
     */
private HashMap<String, Style> commonStyles;
/**
     * A master style is a common style that contains formatting information and
     * additional content that is displayed with the document content when the
     * style is applied. An example of a master style are master pages. Master
     * pages can be used in graphical applications. In this case, the additional
     * content is any drawing shapes that are displayed as the background of the
     * draw page. Master pages can also be used in text documents. In this case,
     * the additional content is the headers and footers. Please note that the
     * content that is contained within master styles is additional content that
     * influences the representation of a document but does not change the
     * content of a document.
     */
private HashMap<String, Style> masterStyles;
/**
     * An automatic style contains formatting properties that, in the user
     * interface view of a document, are assigned to an object such as a
     * paragraph. The term automatic indicates that the style is generated
     * automatically. In other words, formatting properties that are immediately
     * assigned to a specific object are represented by an automatic style. This
     * way, a separation of content and layout is achieved.
     */
private HashMap<String, Style> automaticStyles;
}
