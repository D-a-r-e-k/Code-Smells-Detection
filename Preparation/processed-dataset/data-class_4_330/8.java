/**
     * Sometimes an event causes the source (and display) fields in an
     * accumulated model message to become invalid (e.g. Europe disappears
     * on independence).  This routine is for cleaning up such cases.
     *
     * @param source the source field that has become invalid
     * @param newSource a new source field to replace the old with, or
     *   if null then remove the message
     */
public void divertModelMessages(FreeColGameObject source, FreeColGameObject newSource) {
    // Since we are changing the list, we need to copy it to be able 
    // to iterate through it 
    List<ModelMessage> modelMessagesList = new ArrayList<ModelMessage>();
    modelMessagesList.addAll(modelMessages);
    for (ModelMessage modelMessage : modelMessagesList) {
        if (modelMessage.getSourceId() == source.getId()) {
            if (newSource == null) {
                modelMessages.remove(modelMessage);
            } else {
                modelMessage.divert(newSource);
            }
        }
    }
}
