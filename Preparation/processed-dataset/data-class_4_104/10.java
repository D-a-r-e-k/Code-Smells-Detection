/**
	 * util-method for checking long config-values
	 * @param p
	 * @param def
	 * @return
	 */
private long checkProperty(String p, long def) {
    String pval = props.getProperty(p);
    if (pval == null)
        return def;
    try {
        return Long.parseLong(pval, 10);
    } catch (Exception e) {
        Server.debug("Server", "invalid value specified for configuration-parameter " + pval, e, Server.MSG_ERROR, Server.LVL_MAJOR);
        return def;
    }
}
