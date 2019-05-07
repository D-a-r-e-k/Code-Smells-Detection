/**
     * <p>Called by the cache administrator class when a cache is instantiated.</p>
     * <p>The JMS broadcasting implementation requires the following configuration
     * properties to be specified in <code>oscache.properties</code>:
     * <ul>
     * <li><b>cache.cluster.jms.topic.factory</b> - The JMS connection factory to use</li>
     * <li><b>cache.cluster.jms.topic.name</b> - The JMS topic name</li>
     * <li><b>cache.cluster.jms.node.name</b> - The name of this node in the cluster. This
     * should be unique for each node.</li>
     * Please refer to the clustering documentation for further details on configuring
     * the JMS clustered caching.</p>
     *
     * @param cache the cache instance that this listener is attached to.
     *
     * @throws com.opensymphony.oscache.base.InitializationException thrown when there was a
     * problem initializing the listener. The cache administrator will log this error and
     * disable the listener.
     */
public void initialize(Cache cache, Config config) throws InitializationException {
    super.initialize(cache, config);
    // Get the name of this node  
    clusterNode = config.getProperty("cache.cluster.jms.node.name");
    String topic = config.getProperty("cache.cluster.jms.topic.name");
    String topicFactory = config.getProperty("cache.cluster.jms.topic.factory");
    if (log.isInfoEnabled()) {
        log.info("Starting JMS clustering (node name=" + clusterNode + ", topic=" + topic + ", topic factory=" + topicFactory + ")");
    }
    try {
        // Make sure you have specified the necessary JNDI properties (usually in  
        // a jndi.properties resource file, or as system properties)  
        InitialContext jndi = getInitialContext();
        // Look up a JMS connection factory  
        ConnectionFactory connectionFactory = (ConnectionFactory) jndi.lookup(topicFactory);
        // Create a JMS connection  
        connection = connectionFactory.createConnection();
        // Create session objects  
        publisherSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Session subSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // Look up the JMS topic  
        Topic chatTopic = (Topic) jndi.lookup(topic);
        // Create the publisher and subscriber  
        messagePublisher = publisherSession.createProducer(chatTopic);
        MessageConsumer messageConsumer = subSession.createConsumer(chatTopic);
        // Set the message listener  
        messageConsumer.setMessageListener(new MessageListener() {

            public void onMessage(Message message) {
                try {
                    //check the message type  
                    ObjectMessage objectMessage = null;
                    if (!(message instanceof ObjectMessage)) {
                        log.error("Cannot handle message of type (class=" + message.getClass().getName() + "). Notification ignored.");
                        return;
                    }
                    objectMessage = (ObjectMessage) message;
                    //check the message content  
                    if (!(objectMessage.getObject() instanceof ClusterNotification)) {
                        log.error("An unknown cluster notification message received (class=" + objectMessage.getObject().getClass().getName() + "). Notification ignored.");
                        return;
                    }
                    if (log.isDebugEnabled()) {
                        log.debug(objectMessage.getObject());
                    }
                    // This prevents the notification sent by this node from being handled by itself  
                    if (!objectMessage.getStringProperty("nodeName").equals(clusterNode)) {
                        //now handle the message  
                        ClusterNotification notification = (ClusterNotification) objectMessage.getObject();
                        handleClusterNotification(notification);
                    }
                } catch (JMSException jmsEx) {
                    log.error("Cannot handle cluster Notification", jmsEx);
                }
            }
        });
        // Start the JMS connection; allows messages to be delivered  
        connection.start();
    } catch (Exception e) {
        throw new InitializationException("Initialization of the JMSBroadcastingListener failed: " + e);
    }
}
