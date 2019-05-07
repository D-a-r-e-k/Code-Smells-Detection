/**
     *  Returns the name of the hash field to be used in this request.
     *  The value is unique per session, and once the session has expired,
     *  you cannot edit anymore.
     *
     *  @param request The page request
     *  @return The name to be used in the hash field
     *  @since  2.6
     */
public static final String getHashFieldName(HttpServletRequest request) {
    String hash = null;
    if (request.getSession() != null) {
        hash = (String) request.getSession().getAttribute("_hash");
        if (hash == null) {
            hash = c_hashName;
            request.getSession().setAttribute("_hash", hash);
        }
    }
    if (c_hashName == null || c_lastUpdate < (System.currentTimeMillis() - HASH_DELAY * 60 * 60 * 1000)) {
        c_hashName = getUniqueID().toLowerCase();
        c_lastUpdate = System.currentTimeMillis();
    }
    return hash != null ? hash : c_hashName;
}
