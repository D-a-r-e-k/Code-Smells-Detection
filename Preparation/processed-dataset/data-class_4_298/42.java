void delete(PersistentStore store, NodeAVL x) {
    if (x == null) {
        return;
    }
    NodeAVL n;
    writeLock.lock();
    store.lock();
    try {
        if (x.getLeft(store) == null) {
            n = x.getRight(store);
        } else if (x.getRight(store) == null) {
            n = x.getLeft(store);
        } else {
            NodeAVL d = x;
            x = x.getLeft(store);
            while (true) {
                NodeAVL temp = x.getRight(store);
                if (temp == null) {
                    break;
                }
                x = temp;
            }
            // x will be replaced with n later 
            n = x.getLeft(store);
            // swap d and x 
            int b = x.getBalance(store);
            x = x.setBalance(store, d.getBalance(store));
            d = d.setBalance(store, b);
            // set x.parent 
            NodeAVL xp = x.getParent(store);
            NodeAVL dp = d.getParent(store);
            if (d.isRoot(store)) {
                store.setAccessor(this, x);
            }
            x = x.setParent(store, dp);
            if (dp != null) {
                if (dp.isRight(d)) {
                    dp = dp.setRight(store, x);
                } else {
                    dp = dp.setLeft(store, x);
                }
            }
            // relink d.parent, x.left, x.right 
            if (d.equals(xp)) {
                d = d.setParent(store, x);
                if (d.isLeft(x)) {
                    x = x.setLeft(store, d);
                    NodeAVL dr = d.getRight(store);
                    x = x.setRight(store, dr);
                } else {
                    x = x.setRight(store, d);
                    NodeAVL dl = d.getLeft(store);
                    x = x.setLeft(store, dl);
                }
            } else {
                d = d.setParent(store, xp);
                xp = xp.setRight(store, d);
                NodeAVL dl = d.getLeft(store);
                NodeAVL dr = d.getRight(store);
                x = x.setLeft(store, dl);
                x = x.setRight(store, dr);
            }
            x.getRight(store).setParent(store, x);
            x.getLeft(store).setParent(store, x);
            // set d.left, d.right 
            d = d.setLeft(store, n);
            if (n != null) {
                n = n.setParent(store, d);
            }
            d = d.setRight(store, null);
            x = d;
        }
        boolean isleft = x.isFromLeft(store);
        x.replace(store, this, n);
        n = x.getParent(store);
        x.delete();
        while (n != null) {
            x = n;
            int sign = isleft ? 1 : -1;
            switch(x.getBalance(store) * sign) {
                case -1:
                    x = x.setBalance(store, 0);
                    break;
                case 0:
                    x = x.setBalance(store, sign);
                    return;
                case 1:
                    NodeAVL r = x.child(store, !isleft);
                    int b = r.getBalance(store);
                    if (b * sign >= 0) {
                        x.replace(store, this, r);
                        NodeAVL child = r.child(store, isleft);
                        x = x.set(store, !isleft, child);
                        r = r.set(store, isleft, x);
                        if (b == 0) {
                            x = x.setBalance(store, sign);
                            r = r.setBalance(store, -sign);
                            return;
                        }
                        x = x.setBalance(store, 0);
                        r = r.setBalance(store, 0);
                        x = r;
                    } else {
                        NodeAVL l = r.child(store, isleft);
                        x.replace(store, this, l);
                        b = l.getBalance(store);
                        r = r.set(store, isleft, l.child(store, !isleft));
                        l = l.set(store, !isleft, r);
                        x = x.set(store, !isleft, l.child(store, isleft));
                        l = l.set(store, isleft, x);
                        x = x.setBalance(store, (b == sign) ? -sign : 0);
                        r = r.setBalance(store, (b == -sign) ? sign : 0);
                        l = l.setBalance(store, 0);
                        x = l;
                    }
            }
            isleft = x.isFromLeft(store);
            n = x.getParent(store);
        }
    } finally {
        store.unlock();
        writeLock.unlock();
    }
}
