public ActionstoreObject[] getStoreList() {
    if (storeList.size() < 0) {
        return new ActionstoreObject[0];
    }
    Vector<Object> v = new Vector<Object>();
    for (Enumeration<ActionstoreObject> e = storeList.keys(); e.hasMoreElements(); ) {
        Object o = e.nextElement();
        if (v.contains(o))
            continue;
        v.add(o);
    }
    return (ActionstoreObject[]) v.toArray(new ActionstoreObject[0]);
}
