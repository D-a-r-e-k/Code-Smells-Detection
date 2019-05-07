/**
     * Removes all undisplayed model messages for this player.
     */
public void removeModelMessages() {
    Iterator<ModelMessage> messageIterator = modelMessages.iterator();
    while (messageIterator.hasNext()) {
        ModelMessage message = messageIterator.next();
        if (message.hasBeenDisplayed()) {
            messageIterator.remove();
        }
    }
}
