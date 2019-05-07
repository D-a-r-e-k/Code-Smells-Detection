/**
	 * util-method for checking boolean config-values
	 * @param p
	 * @param def
	 * @return
	 */
private boolean checkProperty(String p, boolean def) {
    String pval = props.getProperty(p);
    if (pval == null)
        return def;
    return pval.equalsIgnoreCase("true");
}
