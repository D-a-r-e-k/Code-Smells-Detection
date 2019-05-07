/**
     * Sets the canvas.
     * If before a set of four canvases was set, it is being unset.
     *
     * @param canvas
     */
public void setCanvas(PdfContentByte canvas) {
    this.canvas = canvas;
    this.canvases = null;
    if (compositeColumn != null)
        compositeColumn.setCanvas(canvas);
}
