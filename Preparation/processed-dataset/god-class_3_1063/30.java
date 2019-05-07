/**
     * Delete all scheduled URIs matching the given regex. 
     * 
     * @param match regex of URIs to delete
     * @return Number of items deleted.
     */
public long deleteURIs(String uriMatch) {
    return deleteURIs(uriMatch, null);
}
