/** Creates a new serial id. */
protected static synchronized Long getSerialId() {
    ++serialId;
    return new Long(serialId);
}
