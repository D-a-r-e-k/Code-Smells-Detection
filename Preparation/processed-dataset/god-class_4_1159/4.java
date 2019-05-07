/**
     * Returns all new ModelMessages for this player.
     *
     * @return all new ModelMessages for this player.
     */
public List<ModelMessage> getNewModelMessages() {
    ArrayList<ModelMessage> out = new ArrayList<ModelMessage>();
    for (ModelMessage message : modelMessages) {
        if (message.hasBeenDisplayed()) {
            continue;
        } else {
            out.add(message);
        }
    }
    return out;
}
