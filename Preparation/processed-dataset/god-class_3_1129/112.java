/**
     * Return if used a concurrent thread pool to get embedded resources.
     *
     * @return true if used
     */
public boolean isConcurrentDwn() {
    return getPropertyAsBoolean(CONCURRENT_DWN, false);
}
