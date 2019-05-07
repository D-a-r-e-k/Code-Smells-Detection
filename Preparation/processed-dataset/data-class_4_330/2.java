/**
     * Adds a <code>ModelMessage</code> for this player.
     *
     * @param modelMessage The <code>ModelMessage</code>.
     */
public void addModelMessage(ModelMessage modelMessage) {
    modelMessage.setOwnerId(getId());
    modelMessages.add(modelMessage);
}
