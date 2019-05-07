public boolean removeStore(Object val, int action) {
    for (Enumeration<?> e = srv.storeList.keys(); e.hasMoreElements(); ) {
        Object key = e.nextElement();
        ActionstoreObject so = (ActionstoreObject) key;
        if (so.usr.equals(val) && so.action == action) {
            if (so.equalsActionState(IActionStates.FLOCKCOL)) {
                User u = UserManager.mgr.getUserByName(so.usr);
                if (u != null)
                    u.setCollock(false);
            }
            if (so.equalsActionState(IActionStates.FLOCKAWAY)) {
                User u = UserManager.mgr.getUserByName(so.usr);
                if (u != null)
                    u.setAwaylock(false);
            }
            if (so.equalsActionState(IActionStates.FLOCKME)) {
                User u = UserManager.mgr.getUserByName(so.usr);
                if (u != null)
                    u.setActlock(false);
            }
            srv.storeList.remove(key);
            so.clearObject();
            return true;
        }
    }
    return false;
}
