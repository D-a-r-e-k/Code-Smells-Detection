/**
     * Sets the serverBanner for the QuickServer
	 * that will be displayed on the standard output [console]
	 * when server starts. <br>&nbsp;<br>
	 * To set welcome message to your client
	 * {@link ClientEventHandler#gotConnected}
     * @param banner for the QuickServer
     * @see #getServerBanner
     */
public void setServerBanner(String banner) {
    serverBanner = banner;
    logger.finest("Set to : " + banner);
}
