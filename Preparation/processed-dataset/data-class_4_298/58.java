/**
     * Balances part of the tree after an alteration to the index.
     */
void balance(PersistentStore store, NodeAVL x, boolean isleft) {
    while (true) {
        int sign = isleft ? 1 : -1;
        switch(x.getBalance(store) * sign) {
            case 1:
                x = x.setBalance(store, 0);
                return;
            case 0:
                x = x.setBalance(store, -sign);
                break;
            case -1:
                NodeAVL l = x.child(store, isleft);
                if (l.getBalance(store) == -sign) {
                    x.replace(store, this, l);
                    x = x.set(store, isleft, l.child(store, !isleft));
                    l = l.set(store, !isleft, x);
                    x = x.setBalance(store, 0);
                    l = l.setBalance(store, 0);
                } else {
                    NodeAVL r = l.child(store, !isleft);
                    x.replace(store, this, r);
                    l = l.set(store, !isleft, r.child(store, isleft));
                    r = r.set(store, isleft, l);
                    x = x.set(store, isleft, r.child(store, !isleft));
                    r = r.set(store, !isleft, x);
                    int rb = r.getBalance(store);
                    x = x.setBalance(store, (rb == -sign) ? sign : 0);
                    l = l.setBalance(store, (rb == sign) ? -sign : 0);
                    r = r.setBalance(store, 0);
                }
                return;
        }
        if (x.isRoot(store)) {
            return;
        }
        isleft = x.isFromLeft(store);
        x = x.getParent(store);
    }
}
