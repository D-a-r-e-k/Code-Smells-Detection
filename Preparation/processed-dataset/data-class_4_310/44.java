private void notifyIteratorsRemovingNode(NodeImpl oldChild) {
    removeStaleIteratorReferences();
    final Iterator i = iterators.iterator();
    while (i.hasNext()) {
        NodeIteratorImpl iterator = (NodeIteratorImpl) ((Reference) i.next()).get();
        if (iterator != null) {
            iterator.removeNode(oldChild);
        } else {
            i.remove();
        }
    }
}
