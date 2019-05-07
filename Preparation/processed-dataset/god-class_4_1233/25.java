/**
	 * Get a font by index
	 *
	 * @param idx
	 *            0-based index of the font
	 * @return of an instance of <code>PPFont</code> or <code>null</code> if not
	 *         found
	 */
public PPFont getFont(int idx) {
    PPFont font = null;
    FontCollection fonts = getDocumentRecord().getEnvironment().getFontCollection();
    Record[] ch = fonts.getChildRecords();
    for (int i = 0; i < ch.length; i++) {
        if (ch[i] instanceof FontEntityAtom) {
            FontEntityAtom atom = (FontEntityAtom) ch[i];
            if (atom.getFontIndex() == idx) {
                font = new PPFont(atom);
                break;
            }
        }
    }
    return font;
}
