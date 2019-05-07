/**
     * Sets the ClientData class that carries client data.
	 * @param data the fully qualified name of the class that 
	 * extends {@link ClientData}.
	 * @see #getClientData
     */
public void setClientData(String data) {
    this.clientDataString = data;
    logger.finest("Set to " + data);
}
