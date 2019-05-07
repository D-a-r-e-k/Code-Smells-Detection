// endDocument(Augmentations) 
/**
     * Consume elements that have been buffered, like </body></html> that are first consumed
     * at the end of document
     */
private void consumeBufferedEndElements() {
    final List toConsume = new ArrayList(endElementsBuffer_);
    endElementsBuffer_.clear();
    for (int i = 0; i < toConsume.size(); ++i) {
        final ElementEntry entry = (ElementEntry) toConsume.get(i);
        forcedEndElement_ = true;
        endElement(entry.name_, entry.augs_);
    }
    endElementsBuffer_.clear();
}
