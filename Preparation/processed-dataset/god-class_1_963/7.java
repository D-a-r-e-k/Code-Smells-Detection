public void setFont(Font font) {
    super.setFont(font);
    Graphics g = this.getGraphics();
    if (g != null) {
        FontMetrics fm = g.getFontMetrics(font);
        int height = fm.getHeight();
        _rowHeight = height + height / 3;
        setRowHeight(_rowHeight);
    }
}
