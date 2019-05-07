/**
     * A method to be called when a text node has been split,
     * so that live objects can be notified.
     */
void splitData(Node node, Node newNode, int offset) {
    // notify ranges  
    if (ranges != null) {
        notifyRangesSplitData(node, newNode, offset);
    }
}
