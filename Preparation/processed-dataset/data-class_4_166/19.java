/**
	 * Paint the renderer.
	 */
public void paint(Graphics g) {
    if (view.isLeaf()) {
        Shape edgeShape = view.getShape();
        // Sideeffect: beginShape, lineShape, endShape 
        if (edgeShape != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            setOpaque(false);
            super.paint(g);
            translateGraphics(g);
            // Hook for pre-painting 
            beforeEdgePaint(g);
            // Actual edge drawing 
            paintEdge(g);
            // Drawing of any selection 
            paintSelection(g);
            // Drawing of labels 
            paintLabels(g);
            // Hook for post-painting 
            afterEdgePaint(g);
        }
    } else {
        paintSelectionBorder(g);
    }
}
