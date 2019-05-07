NodeAVL getAccessor(PersistentStore store) {
    NodeAVL node = (NodeAVL) store.getAccessor(this);
    return node;
}
