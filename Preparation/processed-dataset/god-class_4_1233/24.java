/**
	 * Add a font in this presentation
	 *
	 * @param font
	 *            the font to add
	 * @return 0-based index of the font
	 */
public int addFont(PPFont font) {
    FontCollection fonts = getDocumentRecord().getEnvironment().getFontCollection();
    int idx = fonts.getFontIndex(font.getFontName());
    if (idx == -1) {
        idx = fonts.addFont(font.getFontName(), font.getCharSet(), font.getFontFlags(), font.getFontType(), font.getPitchAndFamily());
    }
    return idx;
}
