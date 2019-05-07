/** 
	 * Information about the service.
	 * @since 1.2
	 */
public String info() {
    serviceError = null;
    StringBuffer buf = new StringBuffer();
    buf.append(getName() + "\n");
    buf.append(getBindAddr().getHostAddress() + " " + getPort() + "\n");
    return buf.toString();
}
