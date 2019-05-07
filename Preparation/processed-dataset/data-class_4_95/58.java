/**
	 * Sets the console log handler formater to 
	 * {@link org.quickserver.util.logging.MicroFormatter}
	 * @since 1.2
	 */
public void setConsoleLoggingToMicro() {
    try {
        setConsoleLoggingFormatter("org.quickserver.util.logging.MicroFormatter");
    } catch (Exception e) {
        logger.warning("Setting to MicroFormatter : " + e);
    }
}
