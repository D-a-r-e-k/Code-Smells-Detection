/**
	 * get the number of fonts in the presentation
	 *
	 * @return number of fonts
	 */
public int getNumberOfFonts() {
    return getDocumentRecord().getEnvironment().getFontCollection().getNumberOfFonts();
}
