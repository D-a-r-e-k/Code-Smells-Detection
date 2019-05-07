/**
    * Initializes the broadcasting listener by starting up a JavaGroups notification
    * bus instance to handle incoming and outgoing messages.
    *
    * @param config An OSCache configuration object.
    * @throws com.opensymphony.oscache.base.InitializationException If this listener has
    * already been initialized.
    */
public synchronized void initialize(Cache cache, Config config) throws InitializationException {
    super.initialize(cache, config);
    String properties = config.getProperty(CHANNEL_PROPERTIES);
    String multicastIP = config.getProperty(MULTICAST_IP_PROPERTY);
    if ((properties == null) && (multicastIP == null)) {
        multicastIP = DEFAULT_MULTICAST_IP;
    }
    if (properties == null) {
        properties = DEFAULT_CHANNEL_PROPERTIES_PRE + multicastIP.trim() + DEFAULT_CHANNEL_PROPERTIES_POST;
    } else {
        properties = properties.trim();
    }
    if (log.isInfoEnabled()) {
        log.info("Starting a new JavaGroups broadcasting listener with properties=" + properties);
    }
    try {
        bus = new NotificationBus(BUS_NAME, properties);
        bus.start();
        bus.getChannel().setOpt(Channel.LOCAL, new Boolean(false));
        bus.setConsumer(this);
        log.info("JavaGroups clustering support started successfully");
    } catch (Exception e) {
        throw new InitializationException("Initialization failed: " + e);
    }
}
