// 
// Constructors 
// 
/** Default constructor. */
public DOMFragmentParser() {
    fParserConfiguration = new HTMLConfiguration();
    fParserConfiguration.addRecognizedFeatures(RECOGNIZED_FEATURES);
    fParserConfiguration.addRecognizedProperties(RECOGNIZED_PROPERTIES);
    fParserConfiguration.setFeature(DOCUMENT_FRAGMENT, true);
    fParserConfiguration.setDocumentHandler(this);
}
