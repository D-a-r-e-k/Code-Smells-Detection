/** 
	 * Returns the closed state of the QuickServer Socket.
	 * @since 1.1
	 */
public boolean isClosed() {
    if (server == null)
        return true;
    return server.isClosed();
}
