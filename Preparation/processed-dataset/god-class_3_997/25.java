/**
	 * Sets the logging level of this class
	 * 
	 * @param level
	 *            the logging level to set
	 */
public void setLoggerLevel(Level level) {
    try {
        logger.setLevel(level);
    } catch (SecurityException e) {
    }
}
