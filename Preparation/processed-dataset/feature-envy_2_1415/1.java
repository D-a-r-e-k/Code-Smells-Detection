protected void installAttributes(Object value, CellView view, boolean isEditing, JGraph graph) {
    if (editorInsideCell && view.getBounds() != null)
        setPreferredSize(new Dimension((int) view.getBounds().getWidth(), (int) view.getBounds().getHeight()));
    if (value instanceof BusinessObjectWrapper2)
        installValue((BusinessObjectWrapper2) value, true);
    else {
        BusinessObjectWrapper2 wrapper = new BusinessObjectWrapper2();
        if (value instanceof DefaultMutableTreeNode)
            wrapper.setValue((DefaultMutableTreeNode) value);
        wrapper.setLabel(value.toString());
        installValue(wrapper, true);
    }
    this.remove(east);
    this.remove(west);
    this.remove(north);
    this.remove(south);
    east = new JPanel(new GridBagLayout());
    west = new JPanel(new GridBagLayout());
    north = new JPanel(new GridBagLayout());
    south = new JPanel(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.weighty = 1.0;
    c.weightx = 1.0;
    c.gridx = 0;
    c.gridy = 0;
    GraphModel model = graph.getModel();
    int childCount = model.getChildCount(view.getCell());
    for (int i = 0; i < childCount; i++) {
        Object child = model.getChild(view.getCell(), i);
        if (model.isPort(child)) {
            CellView portView = graph.getGraphLayoutCache().getMapping(child, false);
            if (portView != null) {
                Point2D point = GraphConstants.getOffset(portView.getAllAttributes());
                String label = (String) model.getValue(portView.getCell());
                JComponent portComponent;
                if (isEditing)
                    portComponent = new JTextField(label);
                else
                    portComponent = new JLabel(label);
                portComponent.setPreferredSize(new Dimension(30, 20));
                if (point.getX() == 0) {
                    //left port 
                    if (point.getY() == 100) {
                        c.anchor = GridBagConstraints.FIRST_LINE_START;
                    } else if (point.getY() == 500) {
                        c.anchor = GridBagConstraints.LINE_START;
                    } else {
                        c.anchor = GridBagConstraints.LAST_LINE_START;
                    }
                    west.add(portComponent, c);
                } else if (point.getX() == GraphConstants.PERMILLE) {
                    //right port 
                    if (point.getY() == 100) {
                        c.anchor = GridBagConstraints.FIRST_LINE_END;
                    } else if (point.getY() == 500) {
                        c.anchor = GridBagConstraints.LINE_END;
                    } else {
                        c.anchor = GridBagConstraints.LAST_LINE_END;
                    }
                    east.add(portComponent, c);
                }
            }
        }
    }
    this.add(east, BorderLayout.EAST);
    this.add(north, BorderLayout.NORTH);
    this.add(west, BorderLayout.WEST);
    this.add(south, BorderLayout.SOUTH);
}
