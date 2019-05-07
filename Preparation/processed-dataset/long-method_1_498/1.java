/**
	 * Sets the debug flag. When debug is set to <code>true</code>
	 * one can see number of bytes written.
	 */
public static void setDebug(boolean flag) {
    if (flag)
        logger.setLevel(Level.FINEST);
    else
        logger.setLevel(Level.INFO);
}
