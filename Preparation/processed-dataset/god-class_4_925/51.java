/** 
	 * Returns the application logger associated with QuickServer.
	 * If it was not set will return QuickServer's own logger.
	 * @since 1.2
	 */
public Logger getAppLogger() {
    if (appLogger != null)
        return appLogger;
    return logger;
}
