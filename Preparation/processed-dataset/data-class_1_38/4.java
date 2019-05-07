// setErrorHandler(ErrorHandler) 
/**
     * Return the current error handler.
     *
     * @return The current error handler, or null if none
     *         has been registered.
     * @see #setErrorHandler
     */
public ErrorHandler getErrorHandler() {
    ErrorHandler errorHandler = null;
    try {
        XMLErrorHandler xmlErrorHandler = (XMLErrorHandler) fParserConfiguration.getProperty(ERROR_HANDLER);
        if (xmlErrorHandler != null && xmlErrorHandler instanceof ErrorHandlerWrapper) {
            errorHandler = ((ErrorHandlerWrapper) xmlErrorHandler).getErrorHandler();
        }
    } catch (XMLConfigurationException e) {
    }
    return errorHandler;
}
