public boolean isActlocked(Object o) {
    // null is not locked-able  
    if (o == null || srv.storeList.size() == 0)
        return false;
    // get the lock-object if there is one  
    Hashtable<?, ?> st = (Hashtable<?, ?>) srv.storeList.clone();
    for (Enumeration<?> e = st.keys(); e.hasMoreElements(); ) {
        Object key = e.nextElement();
        ActionstoreObject l = (ActionstoreObject) key;
        if (l.usr.equals(o) && l.equalsActionState(IActionStates.FLOCKME)) {
            if (l.time < System.currentTimeMillis()) {
                storeList.remove(key);
                l.clearObject();
                return false;
            }
            return true;
        }
        // check if the lock is still fresh  
        if (l.time < System.currentTimeMillis()) {
            storeList.remove(key);
            l.clearObject();
        }
    }
    // get the lock-object if there is one  
    return false;
}
