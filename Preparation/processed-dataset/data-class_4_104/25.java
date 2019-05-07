/**
	 * returns http-url to this server
	 * @return the http-url to this server
	 */
public String getUrl() {
    StringBuffer sb = new StringBuffer();
    sb.append(SERVER_NAME == null ? lh.getCanonicalHostName() : SERVER_NAME.firstElement());
    String port = props.getProperty("mappedPort");
    if (port == null)
        port = props.getProperty("port");
    if (!"80".equals(port)) {
        sb.append(":");
        sb.append(port);
    }
    return (sb.toString());
}
