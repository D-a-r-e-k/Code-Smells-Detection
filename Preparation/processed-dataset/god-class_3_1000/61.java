/**
	 * Variant of the insert method that allows to pass a default connection set
	 * and parent map and nested map.
	 */
public void insert(Object[] cells, Map nested, ConnectionSet cs, ParentMap pm) {
    if (cells != null) {
        if (nested == null)
            nested = new Hashtable();
        if (cs == null)
            cs = new ConnectionSet();
        if (pm == null)
            pm = new ParentMap();
        for (int i = 0; i < cells.length; i++) {
            // Using the children of the vertex we construct the parent map. 
            int childCount = getModel().getChildCount(cells[i]);
            for (int j = 0; j < childCount; j++) {
                Object child = getModel().getChild(cells[i], j);
                pm.addEntry(child, cells[i]);
                // And add their attributes to the nested map 
                AttributeMap attrs = getModel().getAttributes(child);
                if (attrs != null)
                    nested.put(child, attrs);
            }
            // A nested map with the vertex as key 
            // and its attributes as the value 
            // is required for the model. 
            Map attrsTmp = (Map) nested.get(cells[i]);
            Map attrs = getModel().getAttributes(cells[i]);
            if (attrsTmp != null)
                attrs.putAll(attrsTmp);
            nested.put(cells[i], attrs);
            // Check if we have parameters for a connection set. 
            Object sourcePort = getModel().getSource(cells[i]);
            if (sourcePort != null)
                cs.connect(cells[i], sourcePort, true);
            Object targetPort = getModel().getTarget(cells[i]);
            if (targetPort != null)
                cs.connect(cells[i], targetPort, false);
        }
        // Create an array with the parent and its children. 
        cells = DefaultGraphModel.getDescendants(getModel(), cells).toArray();
        // Finally call the insert method on the parent class. 
        insert(cells, nested, cs, pm, null);
    }
}
