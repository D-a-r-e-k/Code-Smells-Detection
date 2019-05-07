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
