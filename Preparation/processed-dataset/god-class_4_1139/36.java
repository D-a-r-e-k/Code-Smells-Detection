/**
     * A method to be called when a character data node has been modified
     */
void modifiedCharacterData(NodeImpl node, String oldvalue, String value, boolean replace) {
    if (mutationEvents) {
        mutationEventsModifiedCharacterData(node, oldvalue, value, replace);
    }
}
