void method0() { 
private static final Log log = LogFactory.getLog(JMSBroadcastingListener.class);
/**
     *The JMS connection used
     */
private Connection connection;
/**
     * Th object used to publish new messages
     */
private MessageProducer messagePublisher;
/**
     * The current JMS session
     */
private Session publisherSession;
/**
     * The name of this cluster. Used to identify the sender of a message.
     */
private String clusterNode;
}
