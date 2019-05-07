/**
   * Returns whether or not this is an Outbox for an OutgoingMailServer.
   */
public boolean isOutboxFolder() {
    return (mailServer != null);
}
