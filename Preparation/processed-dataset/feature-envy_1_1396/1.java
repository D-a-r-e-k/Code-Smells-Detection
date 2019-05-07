/*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constructors.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */
/**
     * Constructor for JobSchedulingDataLoader.
     * 
     * @param clh               class-loader helper to share with digester.
     * @throws ParserConfigurationException if the XML parser cannot be configured as needed. 
     */
public XMLSchedulingDataProcessor(ClassLoadHelper clh) throws ParserConfigurationException {
    this.classLoadHelper = clh;
    initDocumentParser();
}
