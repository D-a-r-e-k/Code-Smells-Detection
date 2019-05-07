void checkNodes(PersistentStore store, NodeAVL p) {
    NodeAVL l = p.getLeft(store);
    NodeAVL r = p.getRight(store);
    if (l != null && l.getBalance(store) == -2) {
        System.out.print("broken index - deleted");
    }
    if (r != null && r.getBalance(store) == -2) {
        System.out.print("broken index -deleted");
    }
    if (l != null && !p.equals(l.getParent(store))) {
        System.out.print("broken index - no parent");
    }
    if (r != null && !p.equals(r.getParent(store))) {
        System.out.print("broken index - no parent");
    }
}
