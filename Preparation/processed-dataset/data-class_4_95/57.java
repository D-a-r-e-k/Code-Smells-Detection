/**
	 * Sets the console log handler formater to 
	 * {@link org.quickserver.util.logging.MiniFormatter}
	 * @since 1.2
	 */
public void setConsoleLoggingToMini() {
    try {
        setConsoleLoggingFormatter("org.quickserver.util.logging.MiniFormatter");
    } catch (Exception e) {
        logger.warning("Setting to logging.MiniFormatter : " + e);
    }
}
