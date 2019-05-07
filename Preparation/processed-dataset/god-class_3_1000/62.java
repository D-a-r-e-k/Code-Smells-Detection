/**
	 * Inserts the specified cell as a parent of children. Note: All cells that
	 * are not yet in the model will be inserted. This example shows how to
	 * group the current selection and pass the group default bounds in case it
	 * is later collapsed:
	 * 
	 * <pre>
	 * DefaultGraphCell group = new DefaultGraphCell(&quot;Hello, world!&quot;);
	 * Object[] cells = DefaultGraphModel.order(graph.getModel(), graph
	 * 		.getSelectionCells());
	 * Rectangle2D bounds = graph.getCellBounds(cells);
	 * if (bounds != null) {
	 * 	bounds = new Rectangle2D.Double(bounds.getX() + bounds.getWidth() / 4,
	 * 			bounds.getY() + bounds.getHeight() / 4, bounds.getWidth() / 2,
	 * 			bounds.getHeight() / 2);
	 * 	GraphConstants.setBounds(group.getAttributes(), bounds);
	 * }
	 * graph.getGraphLayoutCache().insertGroup(group, cells);
	 * </pre>
	 */
public void insertGroup(Object group, Object[] children) {
    if (group != null && children != null && children.length > 0) {
        Map nested = new Hashtable();
        // List to store all children that are not in the model 
        List newCells = new ArrayList(children.length + 1);
        // Plus the group cell at pos 0 
        if (!getModel().contains(group)) {
            newCells.add(group);
        }
        // Create a parent map for the group and the children, and 
        // store the children's attributes in the nested map. 
        // Note: This implementation assumes that the children have 
        // not yet been added to the group object. Therefore, 
        // the insert method will only collect the group 
        // attributes, but will ignore the child attributes. 
        ParentMap pm = new ParentMap();
        for (int i = 0; i < children.length; i++) {
            pm.addEntry(children[i], group);
            if (!getModel().contains(children[i])) {
                newCells.add(children[i]);
                AttributeMap attrs = getModel().getAttributes(children[i]);
                if (attrs != null)
                    nested.put(children[i], attrs);
            }
        }
        if (newCells.isEmpty())
            edit(nested, null, pm, null);
        else
            insert(newCells.toArray(), nested, null, pm);
    }
}
