/**
     * Sets the client socket's timeout.
	 * @param time client socket timeout in milliseconds.
	 * @see #getTimeout
     */
public void setTimeout(int time) {
    if (time > 0)
        socketTimeout = time;
    else
        socketTimeout = 0;
    logger.finest("Set to " + socketTimeout);
}
