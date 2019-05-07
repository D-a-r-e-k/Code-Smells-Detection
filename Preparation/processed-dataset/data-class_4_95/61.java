/**
	 * Sets the level for all log handlers.
	 * @since 1.3.1
	 */
public void setLoggingLevel(Level level) {
    Logger rlogger = Logger.getLogger("");
    Handler[] handlers = rlogger.getHandlers();
    for (int index = 0; index < handlers.length; index++) {
        handlers[index].setLevel(level);
    }
    if (level == Level.SEVERE)
        loggingLevel = "SEVERE";
    else if (level == Level.WARNING)
        loggingLevel = "WARNING";
    else if (level == Level.INFO)
        loggingLevel = "INFO";
    else if (level == Level.CONFIG)
        loggingLevel = "CONFIG";
    else if (level == Level.FINE)
        loggingLevel = "FINE";
    else if (level == Level.FINER)
        loggingLevel = "FINER";
    else if (level == Level.FINEST)
        loggingLevel = "FINEST";
    else
        loggingLevel = "UNKNOWN";
    consoleLoggingLevel = loggingLevel;
    logger.fine("Set to " + level);
}
