void method0() { 
/** Switch for exact label hit detection on rotated labels. Default is false. */
public static boolean HIT_LABEL_EXACT = false;
/** Static Graphics used for Font Metrics */
protected static transient Graphics fontGraphics;
/** When zooming a graph the font size jumps at certain zoom levels rather than
	 *  scaling smoothly. Sometimes the zoom on the font is more than the component zoom
	 *  and cropping occurs. This buffer allows for the maximum occurance of this
	 */
public static double LABELWIDTHBUFFER = 1.1;
/** A switch for painting the extra labels */
public boolean simpleExtraLabels = true;
/** Override this if you want the extra labels to appear in a special fontJ */
public Font extraLabelFont = null;
/** Reference to the font metrics of the above */
protected transient FontMetrics metrics;
/** Cache the current graph for drawing */
protected transient WeakReference graph;
/** Cache the current edgeview for drawing */
protected transient EdgeView view;
/** Painting attributes of the current edgeview */
protected transient int beginDeco, endDeco, beginSize, endSize, lineStyle;
/** Width of the current edge view */
protected transient float lineWidth;
/** Cached value of whether the label is to be displayed */
protected transient boolean labelsEnabled;
/**
	 * Boolean attributes of the current edgeview. Fill flags are checked for
	 * valid decorations.
	 */
protected transient boolean labelBorder, beginFill, endFill, focus, selected, preview, opaque, childrenSelected, labelTransformEnabled, isMoveBelowZero;
/**
	 * Color attributes of the current edgeview. This components foreground is
	 * set to the edgecolor, the fontColor is in an extra variable. If the
	 * fontColor is null, the current foreground is used. The default background
	 * instead is used for text and is not visible if the label is not visible
	 * or if opaque is true.
	 */
protected transient Color borderColor, defaultForeground, defaultBackground, fontColor;
/** Contains the current dash pattern. Null means no pattern. */
protected transient float[] lineDash;
/** Contains the current dash offset. Null means no offset. */
protected transient float dashOffset = 0.0f;
/** The gradient color of the edge */
protected transient Color gradientColor = null;
/** The color of the graph grid */
protected transient Color gridColor = null;
/** The color of the second available handle */
protected transient Color lockedHandleColor = null;
/** The color of highlighted cells */
protected transient Color highlightColor = null;
/** Cached bezier curve */
protected transient Bezier bezier;
/** Cached spline curve */
protected transient Spline2D spline;
}
