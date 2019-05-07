/**
	 * Creates a local edit for the specified change. A local operation contains
	 * all visibility changes, as well as all changes to attributes that are
	 * local, and all control attributes. <br>
	 * Note: You must use cells as keys for the nested map, not cell views.
	 */
protected GraphLayoutCacheEdit createLocalEdit(Object[] inserted, Map nested, Object[] visible, Object[] invisible) {
    // Create an edit if there are any view-local attributes set 
    if ((nested != null && !nested.isEmpty()) && (!localAttributes.isEmpty() || isAllAttributesLocal())) {
        // Move or Copy Local Attributes to Local View 
        Map globalMap = new Hashtable();
        Map localMap = new Hashtable();
        Map localAttr;
        Iterator it = nested.entrySet().iterator();
        while (it.hasNext()) {
            localAttr = new Hashtable();
            Map.Entry entry = (Map.Entry) it.next();
            // (cell, Hashtable) 
            Object cell = entry.getKey();
            Map attr = (Map) entry.getValue();
            // Create Difference of Existing and New Attributes 
            CellView tmpView = getMapping(cell, false);
            if (tmpView != null)
                attr = tmpView.getAllAttributes().diff(attr);
            // End of Diff 
            Iterator it2 = attr.entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry entry2 = (Map.Entry) it2.next();
                // (key, value) 
                Object key = entry2.getKey();
                Object value = entry2.getValue();
                boolean isControlAttribute = isControlAttribute(cell, key, value);
                if (isAllAttributesLocal() || isControlAttribute || isLocalAttribute(cell, key, value)) {
                    localAttr.put(key, value);
                    if (!isControlAttribute)
                        it2.remove();
                }
            }
            if (!localAttr.isEmpty())
                localMap.put(cell, localAttr);
            if (!attr.isEmpty())
                globalMap.put(cell, attr);
        }
        nested.clear();
        nested.putAll(globalMap);
        if (visible != null || invisible != null || !localMap.isEmpty()) {
            GraphLayoutCacheEdit edit = new GraphLayoutCacheEdit(inserted, new Hashtable(localMap), visible, invisible);
            edit.end();
            return edit;
        }
    } else if (visible != null || invisible != null) {
        GraphLayoutCacheEdit edit = new GraphLayoutCacheEdit(inserted, null, visible, invisible);
        edit.end();
        return edit;
    }
    return null;
}
