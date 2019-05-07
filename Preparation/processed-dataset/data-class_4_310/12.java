/**
     * A method to be called when some text was changed in a text node,
     * so that live objects can be notified.
     */
void replacedText(CharacterDataImpl node) {
    // notify ranges  
    if (ranges != null) {
        notifyRangesReplacedText(node);
    }
}
