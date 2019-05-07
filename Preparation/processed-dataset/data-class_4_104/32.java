public BanObject[] getBanList() {
    if (banList.size() < 0) {
        return new BanObject[0];
    }
    Vector<Object> v = new Vector<Object>();
    for (Enumeration<BanObject> e = banList.elements(); e.hasMoreElements(); ) {
        Object o = e.nextElement();
        //if (v.contains(o))  
        //    continue;  
        v.add(o);
    }
    return (BanObject[]) v.toArray(new BanObject[0]);
}
