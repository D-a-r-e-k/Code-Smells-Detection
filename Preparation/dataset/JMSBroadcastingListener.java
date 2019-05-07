/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.oscache.plugins.clustersupport;

import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.base.Config;
import com.opensymphony.oscache.base.FinalizationException;
import com.opensymphony.oscache.base.InitializationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.jms.*;

import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * A JMS based clustering implementation. This implementation is independent of the
 * JMS provider and uses non-persistent messages on a publish subscribe protocol.
 *
 * @author <a href="mailto:motoras@linuxmail.org">Romulus Pasca</a>
 */
public class JMSBroadcastingListener extends AbstractBroadcastingListener {
    
    private final static Log log = LogFactory.getLog(JMSBroadcastingListener.class);

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

    /**
     * Called by the cache administrator class when a cache is destroyed.
     *
     * @throws com.opensymphony.oscache.base.FinalizationException thrown when there was a problem finalizing the
     * listener. The cache administrator will catch and log this error.
     */
    public void finialize() throws FinalizationException {
        try {
            if (log.isInfoEnabled()) {
                log.info("Shutting down JMS clustering...");
            }

            connection.close();

            if (log.isInfoEnabled()) {
                log.info("JMS clustering shutdown complete.");
            }
        } catch (JMSException e) {
            log.warn("A problem was encountered when closing the JMS connection", e);
        }
    }

    protected void sendNotification(ClusterNotification message) {
        try {
            ObjectMessage objectMessage = publisherSession.createObjectMessage();
            objectMessage.setObject(message);

            //sign the message, with the name of this node
            objectMessage.setStringProperty("nodeName", clusterNode);
            messagePublisher.send(objectMessage);
        } catch (JMSException e) {
            log.error("Cannot send notification " + message, e);
        }
    }
    
    /**
     * @return creates a context for performing naming operations.
     * @throws NamingException if a naming exception is encountered
     */
    protected InitialContext getInitialContext() throws NamingException {
        return new InitialContext();
    }

}
