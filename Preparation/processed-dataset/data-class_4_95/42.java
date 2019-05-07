/** 
	 * Set the port to run QSAdminServer on.
	 * @since 1.2
	 */
public void setQSAdminServerPort(int port) {
    getQSAdminServer().getServer().setPort(port);
}
