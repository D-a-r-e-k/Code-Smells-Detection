NodeAVL next(PersistentStore store, NodeAVL x) {
    NodeAVL r = x.getRight(store);
    if (r != null) {
        x = r;
        NodeAVL l = x.getLeft(store);
        while (l != null) {
            x = l;
            l = x.getLeft(store);
        }
        return x;
    }
    NodeAVL ch = x;
    x = x.getParent(store);
    while (x != null && ch.equals(x.getRight(store))) {
        ch = x;
        x = x.getParent(store);
    }
    return x;
}
