/**
	 * Returns the bounds of the edge shape without label
	 */
public Rectangle2D getPaintBounds(EdgeView view) {
    Rectangle2D rec = null;
    setView(view);
    if (view.getShape() != null)
        rec = view.getShape().getBounds();
    else
        rec = new Rectangle2D.Double(0, 0, 0, 0);
    return rec;
}
