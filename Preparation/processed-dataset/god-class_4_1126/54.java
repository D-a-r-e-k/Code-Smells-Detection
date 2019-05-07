NodeAVL last(PersistentStore store, NodeAVL x) {
    if (x == null) {
        return null;
    }
    NodeAVL left = x.getLeft(store);
    if (left != null) {
        x = left;
        NodeAVL right = x.getRight(store);
        while (right != null) {
            x = right;
            right = x.getRight(store);
        }
        return x;
    }
    NodeAVL ch = x;
    x = x.getParent(store);
    while (x != null && ch.equals(x.getLeft(store))) {
        ch = x;
        x = x.getParent(store);
    }
    return x;
}
