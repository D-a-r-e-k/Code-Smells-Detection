/**
	 * Sets the console log handler level.
	 * @since 1.2
	 */
public void setConsoleLoggingLevel(Level level) {
    Logger rlogger = Logger.getLogger("");
    Handler[] handlers = rlogger.getHandlers();
    for (int index = 0; index < handlers.length; index++) {
        if (ConsoleHandler.class.isInstance(handlers[index])) {
            handlers[index].setLevel(level);
        }
    }
    if (level == Level.SEVERE)
        consoleLoggingLevel = "SEVERE";
    else if (level == Level.WARNING)
        consoleLoggingLevel = "WARNING";
    else if (level == Level.INFO)
        consoleLoggingLevel = "INFO";
    else if (level == Level.CONFIG)
        consoleLoggingLevel = "CONFIG";
    else if (level == Level.FINE)
        consoleLoggingLevel = "FINE";
    else if (level == Level.FINER)
        consoleLoggingLevel = "FINER";
    else if (level == Level.FINEST)
        consoleLoggingLevel = "FINEST";
    else
        consoleLoggingLevel = "UNKNOWN";
    logger.fine("Set to " + level);
}
