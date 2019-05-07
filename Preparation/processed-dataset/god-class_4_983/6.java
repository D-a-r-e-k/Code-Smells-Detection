/**
     * <p>
     * Initialize the <code>{@link org.quartz.SchedulerFactory}</code> with
     * the contents of the given <code>Properties</code> object.
     * </p>
     */
public void initialize(Properties props) throws SchedulerException {
    if (propSrc == null) {
        propSrc = "an externally provided properties instance.";
    }
    this.cfg = new PropertiesParser(props);
}
