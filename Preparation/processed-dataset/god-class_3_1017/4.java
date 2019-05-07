/**
     * @param corner The cornerButton to set.
     */
public void setCorner(JComponent corner) {
    this.corner = corner;
    corner.setPreferredSize(new Dimension(verticalScrollBar.getPreferredSize().width, horizontalScrollBar.getPreferredSize().height));
    south.add(this.corner, BorderLayout.EAST);
}
