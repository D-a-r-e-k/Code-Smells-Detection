/**
	 * A RichMediaWindow Dictionary that describes the size and
	 * position of the floating user interface window when the
	 * value for Style is set to Windowed.
	 * @param	window	a RichMediaWindow object
	 */
public void setWindow(RichMediaWindow window) {
    put(PdfName.WINDOW, window);
}
