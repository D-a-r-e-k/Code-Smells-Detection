/**
     * Tells the map controls that a chat message was received.
     *
     * @param player The player who sent the chat message.
     * @param message The chat message.
     * @param privateChat 'true' if the message is a private one, 'false'
     *            otherwise.
     * @see GUIMessage
     */
public void displayChatMessage(Player player, String message, boolean privateChat) {
    mapViewer.addMessage(new GUIMessage(player.getName() + ": " + message, imageLibrary.getColor(player)));
    canvas.repaint(0, 0, canvas.getWidth(), canvas.getHeight());
}
