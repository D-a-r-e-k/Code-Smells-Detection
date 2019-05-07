/**
	 * Set a flag that indicates whether the page content is displayed
	 * through the transparent areas of the rich media content (where
	 * the alpha value is less than 1.0). If true, the rich media artwork
	 * is composited over the page content using an alpha channel. If false,
	 * the rich media artwork is drawn over an opaque background prior to
	 * composition over the page content.
	 * @param	transparent	a boolean
	 */
public void setTransparent(PdfBoolean transparent) {
    put(PdfName.TRANSPARENT, transparent);
}
