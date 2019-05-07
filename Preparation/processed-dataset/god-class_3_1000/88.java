/**
	 * Attention: Undo will not work for routing-change if ROUTING and POINTS
	 * are stored in different locations. This happens if the model holds the
	 * routing attribute and the routing changes from unrouted to routed. In
	 * this case the points in the view are already routed according to the new
	 * scheme when written to the command history (-> no undo).
	 */
protected Map handleAttributes(Map attributes) {
    Map undo = new Hashtable();
    CellView[] views = new CellView[attributes.size()];
    Iterator it = attributes.entrySet().iterator();
    int i = 0;
    while (it.hasNext()) {
        Map.Entry entry = (Map.Entry) it.next();
        CellView cv = getMapping(entry.getKey(), false);
        views[i] = cv;
        i += 1;
        if (cv != null && cv.getAttributes() != null) {
            Map deltaNew = (Map) entry.getValue();
            // System.out.println("state=" + cv.getAttributes()); 
            // System.out.println("change=" + deltaNew); 
            Map deltaOld = cv.getAttributes().applyMap(deltaNew);
            cv.refresh(this, this, false);
            // System.out.println("state'=" + cv.getAttributes()); 
            // System.out.println("change'=" + deltaOld); 
            undo.put(cv.getCell(), deltaOld);
        }
    }
    // Re-route all child edges 
    update(views);
    return undo;
}
