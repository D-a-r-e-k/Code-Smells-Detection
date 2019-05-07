// saveEnclosingAttr(NodeImpl) :void  
/**
     * A method to be called when a character data node has been modified
     */
void modifyingCharacterData(NodeImpl node, boolean replace) {
    if (mutationEvents) {
        if (!replace) {
            saveEnclosingAttr(node);
        }
    }
}
