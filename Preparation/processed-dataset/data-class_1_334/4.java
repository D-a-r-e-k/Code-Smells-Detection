/** We only need to re-implement the painter for the GlyphView to modify its behavior. The GlyphView delegates its 
    * paint method to the painter.  It also allows the painter to obtain the document to which the element
    * belongs.
    * @param elem The Element to pass to the GlyphView
    * @return A GlyphView with modified behavior
    */
private static GlyphView _createColoringView(Element elem) {
    final GlyphView view = new GlyphView(elem);
    view.setGlyphPainter(new ColoringGlyphPainter(new Runnable() {

        public void run() {
            if (view.getContainer() != null)
                view.getContainer().repaint();
        }
    }));
    return view;
}
