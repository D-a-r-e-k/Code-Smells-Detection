/** 
	 * Returns number of clients connected.
	 * @since 1.1
	 */
public long getClientCount() {
    if (clientHandlerPool != null) {
        try {
            return getClientHandlerPool().getNumActive();
        } catch (Exception e) {
            return 0;
        }
    }
    return 0;
}
