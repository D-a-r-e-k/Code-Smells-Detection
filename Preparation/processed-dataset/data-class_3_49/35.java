/**
     * Helper method for entrySet remove.
     **/
protected synchronized boolean findAndRemoveEntry(Map.Entry entry) {
    Object key = entry.getKey();
    Object v = get(key);
    if ((v != null) && v.equals(entry.getValue())) {
        remove(key);
        return true;
    } else {
        return false;
    }
}
