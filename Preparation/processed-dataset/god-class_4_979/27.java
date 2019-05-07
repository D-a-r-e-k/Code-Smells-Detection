/**
     * Returns a <code>List</code> of triggers loaded from the xml file.
     * <p/>
     * 
     * @return a <code>List</code> of triggers.
     */
protected List<Trigger> getLoadedTriggers() {
    return Collections.unmodifiableList(loadedTriggers);
}
