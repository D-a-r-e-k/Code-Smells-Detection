/**
	 * util-method for checking String config-values
	 * @param p
	 * @param def
	 * @return
	 */
private String checkProperty(String p, String def) {
    String pval = props.getProperty(p);
    if (pval == null)
        return def;
    return pval;
}
