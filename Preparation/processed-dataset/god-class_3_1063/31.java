/**
     * Delete all scheduled URIs matching the given regex, in queues with
     * names matching the second given regex. 
     * 
     * @param uriMatch regex of URIs to delete
     * @param queueMatch regex of queues to affect, or null for all
     * @return Number of items deleted.
     */
public long deleteURIs(String uriMatch, String queueMatch) {
    long count = 0;
    // TODO: DANGER/ values() may not work right from CachedBdbMap 
    Iterator<String> iter = allQueues.keySet().iterator();
    while (iter.hasNext()) {
        String queueKey = ((String) iter.next());
        if (StringUtils.isNotEmpty(queueMatch) && !queueKey.matches(queueMatch)) {
            // skip this queue 
            continue;
        }
        WorkQueue wq = getQueueFor(queueKey);
        wq.unpeek();
        count += wq.deleteMatching(this, uriMatch);
    }
    decrementQueuedCount(count);
    return count;
}
