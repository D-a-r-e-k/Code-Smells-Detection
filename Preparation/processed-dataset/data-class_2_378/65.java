/**
     * Provides serialization support.
     *
     * @param stream  the input stream.
     *
     * @throws IOException  if there is an I/O error.
     * @throws ClassNotFoundException  if there is a classpath problem.
     */
private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    this.legendItemShape = SerialUtilities.readShape(stream);
    this.seriesPaint = SerialUtilities.readPaint(stream);
    this.baseSeriesPaint = SerialUtilities.readPaint(stream);
    this.seriesOutlinePaint = SerialUtilities.readPaint(stream);
    this.baseSeriesOutlinePaint = SerialUtilities.readPaint(stream);
    this.seriesOutlineStroke = SerialUtilities.readStroke(stream);
    this.baseSeriesOutlineStroke = SerialUtilities.readStroke(stream);
    this.labelPaint = SerialUtilities.readPaint(stream);
    this.axisLinePaint = SerialUtilities.readPaint(stream);
    this.axisLineStroke = SerialUtilities.readStroke(stream);
    if (this.dataset != null) {
        this.dataset.addChangeListener(this);
    }
}
