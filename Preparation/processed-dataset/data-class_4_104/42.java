public boolean isPunished(Object o) {
    // null is not punish-able  
    if (o == null || srv.storeList.size() == 0)
        return false;
    // get the punish-object if there is one  
    Hashtable<?, ?> st = (Hashtable<?, ?>) srv.storeList.clone();
    for (Enumeration<?> e = st.keys(); e.hasMoreElements(); ) {
        Object key = e.nextElement();
        ActionstoreObject p = (ActionstoreObject) key;
        if (p.usr.equals(o) && p.equalsActionState(IActionStates.PUNISH)) {
            if (p.time < System.currentTimeMillis()) {
                storeList.remove(key);
                p.clearObject();
                return false;
            }
            return true;
        }
        //	check if the punish is still fresh  
        if (p.time < System.currentTimeMillis()) {
            storeList.remove(key);
            p.clearObject();
        }
    }
    // not-punished if there is no punish-object  
    return false;
}
