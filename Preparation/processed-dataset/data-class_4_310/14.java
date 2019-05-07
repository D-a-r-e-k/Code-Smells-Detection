/**
     * A method to be called when some text was deleted from a text node,
     * so that live objects can be notified.
     */
void deletedText(CharacterDataImpl node, int offset, int count) {
    // notify ranges  
    if (ranges != null) {
        notifyRangesDeletedText(node, offset, count);
    }
}
