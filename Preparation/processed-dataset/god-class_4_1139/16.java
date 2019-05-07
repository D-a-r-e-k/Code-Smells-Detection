/**
     * A method to be called when some text was inserted into a text node,
     * so that live objects can be notified.
     */
void insertedText(CharacterDataImpl node, int offset, int count) {
    // notify ranges  
    if (ranges != null) {
        notifyRangesInsertedText(node, offset, count);
    }
}
