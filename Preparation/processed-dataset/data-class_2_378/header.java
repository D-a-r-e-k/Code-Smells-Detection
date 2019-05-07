void method0() { 
/** For serialization. */
private static final long serialVersionUID = -5376340422031599463L;
/** The default head radius percent (currently 1%). */
public static final double DEFAULT_HEAD = 0.01;
/** The default axis label gap (currently 10%). */
public static final double DEFAULT_AXIS_LABEL_GAP = 0.10;
/** The default interior gap. */
public static final double DEFAULT_INTERIOR_GAP = 0.25;
/** The maximum interior gap (currently 40%). */
public static final double MAX_INTERIOR_GAP = 0.40;
/** The default starting angle for the radar chart axes. */
public static final double DEFAULT_START_ANGLE = 90.0;
/** The default series label font. */
public static final Font DEFAULT_LABEL_FONT = new Font("SansSerif", Font.PLAIN, 10);
/** The default series label paint. */
public static final Paint DEFAULT_LABEL_PAINT = Color.black;
/** The default series label background paint. */
public static final Paint DEFAULT_LABEL_BACKGROUND_PAINT = new Color(255, 255, 192);
/** The default series label outline paint. */
public static final Paint DEFAULT_LABEL_OUTLINE_PAINT = Color.black;
/** The default series label outline stroke. */
public static final Stroke DEFAULT_LABEL_OUTLINE_STROKE = new BasicStroke(0.5f);
/** The default series label shadow paint. */
public static final Paint DEFAULT_LABEL_SHADOW_PAINT = Color.lightGray;
/**
     * The default maximum value plotted - forces the plot to evaluate
     *  the maximum from the data passed in
     */
public static final double DEFAULT_MAX_VALUE = -1.0;
/** The head radius as a percentage of the available drawing area. */
protected double headPercent;
/** The space left around the outside of the plot as a percentage. */
private double interiorGap;
/** The gap between the labels and the axes as a %age of the radius. */
private double axisLabelGap;
/**
     * The paint used to draw the axis lines.
     *
     * @since 1.0.4
     */
private transient Paint axisLinePaint;
/**
     * The stroke used to draw the axis lines.
     *
     * @since 1.0.4
     */
private transient Stroke axisLineStroke;
/** The dataset. */
private CategoryDataset dataset;
/** The maximum value we are plotting against on each category axis */
private double maxValue;
/**
     * The data extract order (BY_ROW or BY_COLUMN). This denotes whether
     * the data series are stored in rows (in which case the category names are
     * derived from the column keys) or in columns (in which case the category
     * names are derived from the row keys).
     */
private TableOrder dataExtractOrder;
/** The starting angle. */
private double startAngle;
/** The direction for drawing the radar axis & plots. */
private Rotation direction;
/** The legend item shape. */
private transient Shape legendItemShape;
/** The paint for ALL series (overrides list). */
private transient Paint seriesPaint;
/** The series paint list. */
private PaintList seriesPaintList;
/** The base series paint (fallback). */
private transient Paint baseSeriesPaint;
/** The outline paint for ALL series (overrides list). */
private transient Paint seriesOutlinePaint;
/** The series outline paint list. */
private PaintList seriesOutlinePaintList;
/** The base series outline paint (fallback). */
private transient Paint baseSeriesOutlinePaint;
/** The outline stroke for ALL series (overrides list). */
private transient Stroke seriesOutlineStroke;
/** The series outline stroke list. */
private StrokeList seriesOutlineStrokeList;
/** The base series outline stroke (fallback). */
private transient Stroke baseSeriesOutlineStroke;
/** The font used to display the category labels. */
private Font labelFont;
/** The color used to draw the category labels. */
private transient Paint labelPaint;
/** The label generator. */
private CategoryItemLabelGenerator labelGenerator;
/** controls if the web polygons are filled or not */
private boolean webFilled = true;
/** A tooltip generator for the plot (<code>null</code> permitted). */
private CategoryToolTipGenerator toolTipGenerator;
/** A URL generator for the plot (<code>null</code> permitted). */
private CategoryURLGenerator urlGenerator;
}
