/** 
	 * Sets max allowed login attempts.
	 * @since 1.2
	 * @see #getMaxAuthTry
	 */
public void setMaxAuthTry(int authTry) {
    maxAuthTry = authTry;
    logger.finest("Set to " + authTry);
}
