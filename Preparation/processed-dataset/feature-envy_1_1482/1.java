/**
     * @param name
     * @param description
     */
public AbstractTracker(String name, String description) {
    super(name, description);
    Type e = addElementToDefinition(new SimpleType(ATTR_STATS_INTERVAL, "The interval between writing progress information to log.", DEFAULT_STATISTICS_REPORT_INTERVAL));
    e.setOverrideable(false);
}
