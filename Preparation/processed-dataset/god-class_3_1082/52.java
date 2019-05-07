/**
     * Sets the canvases.
     *
     * @param canvases
     */
public void setCanvases(PdfContentByte[] canvases) {
    this.canvases = canvases;
    this.canvas = canvases[PdfPTable.TEXTCANVAS];
    if (compositeColumn != null)
        compositeColumn.setCanvases(canvases);
}
