/**
	 * Sets the console log handler formatter.
	 * @param formatter fully qualified name of the class that implements 
	 * {@link java.util.logging.Formatter}
	 * @since 1.2
	 */
public void setConsoleLoggingFormatter(String formatter) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    if (formatter == null)
        return;
    consoleLoggingformatter = formatter;
    Formatter conformatter = (Formatter) getClass(formatter, true).newInstance();
    Logger jdkLogger = Logger.getLogger("");
    Handler[] handlers = jdkLogger.getHandlers();
    for (int index = 0; index < handlers.length; index++) {
        if (ConsoleHandler.class.isInstance(handlers[index])) {
            handlers[index].setFormatter(conformatter);
        }
    }
    logger.finest("Set to " + formatter);
}
