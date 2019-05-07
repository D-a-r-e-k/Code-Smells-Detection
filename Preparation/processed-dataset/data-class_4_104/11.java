/**
	 * util-method for checking int config-values
	 * @param p
	 * @param def
	 * @return
	 */
private int checkProperty(String p, int def) {
    String pval = props.getProperty(p);
    if (pval == null)
        return def;
    try {
        return Integer.parseInt(pval.trim(), 10);
    } catch (Exception e) {
        Server.debug("Server", "invalid value specified for configuration-parameter " + pval, e, Server.MSG_ERROR, Server.LVL_MAJOR);
        return def;
    }
}
