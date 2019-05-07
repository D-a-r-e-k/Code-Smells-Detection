private void createAnnotationValidator() {
    fAnnotationValidator = new XML11Configuration();
    fGrammarBucketAdapter = new XSAnnotationGrammarPool();
    fAnnotationValidator.setFeature(VALIDATION, true);
    fAnnotationValidator.setFeature(XMLSCHEMA_VALIDATION, true);
    fAnnotationValidator.setProperty(XMLGRAMMAR_POOL, fGrammarBucketAdapter);
    /** Set error handler. **/
    XMLErrorHandler errorHandler = fErrorReporter.getErrorHandler();
    fAnnotationValidator.setProperty(ERROR_HANDLER, (errorHandler != null) ? errorHandler : new DefaultErrorHandler());
    /** Set locale. **/
    Locale locale = fErrorReporter.getLocale();
    fAnnotationValidator.setProperty(LOCALE, locale);
}
