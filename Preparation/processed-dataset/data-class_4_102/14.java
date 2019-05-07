/**
     * replaces every < and every > with an HTML-entity and returns the value
     * @param rs
     * @param c
     * @return String The string having the replaces < and >-characters
     */
private static String getEncodedString(ResultSet rs, int idx) {
    if (rs == null)
        return null;
    try {
        String result = rs.getString(idx);
        if (result == null)
            return null;
        result = result.replaceAll("[<]", "&lt;");
        result = result.replaceAll("[>]", "&gt;");
        result = EntityDecoder.convertFormattingCharacters(result);
        return result;
    } catch (Exception e) {
        Server.debug("static PoolElement", "getEncodedString: error geting encoded string", e, Server.MSG_ERROR, Server.LVL_MAJOR);
    }
    return null;
}
