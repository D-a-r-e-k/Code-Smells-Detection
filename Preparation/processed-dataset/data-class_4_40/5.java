// getRecognizedProperties():String[] 
/** Resets the component. */
public void reset(XMLComponentManager manager) throws XMLConfigurationException {
    // get features 
    fNamespaces = manager.getFeature(NAMESPACES);
    fAugmentations = manager.getFeature(AUGMENTATIONS);
    fReportErrors = manager.getFeature(REPORT_ERRORS);
    fDocumentFragment = manager.getFeature(DOCUMENT_FRAGMENT) || manager.getFeature(DOCUMENT_FRAGMENT_DEPRECATED);
    fIgnoreOutsideContent = manager.getFeature(IGNORE_OUTSIDE_CONTENT);
    // get properties 
    fNamesElems = getNamesValue(String.valueOf(manager.getProperty(NAMES_ELEMS)));
    fNamesAttrs = getNamesValue(String.valueOf(manager.getProperty(NAMES_ATTRS)));
    fErrorReporter = (HTMLErrorReporter) manager.getProperty(ERROR_REPORTER);
    fragmentContextStack_ = (QName[]) manager.getProperty(FRAGMENT_CONTEXT_STACK);
}
